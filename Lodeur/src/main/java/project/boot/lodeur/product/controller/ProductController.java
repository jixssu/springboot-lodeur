package project.boot.lodeur.product.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import project.boot.lodeur.member.entity.MemberEntity;
import project.boot.lodeur.member.repository.MemberRepository;
import project.boot.lodeur.product.entity.ProductEntity;
import project.boot.lodeur.product.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final MemberRepository memberRepository;

	@Autowired
	public ProductController(ProductService productService, MemberRepository memberRepository) {
		this.productService = productService;
		this.memberRepository = memberRepository;
	}

	// 제품 생성 폼을 보여줍니다.
	@GetMapping("/save")
	public String showCreateForm(Model model) {
		model.addAttribute("productEntity", new ProductEntity());
		return "product/save";
	}

	// 새로운 제품을 생성합니다.
	@PostMapping
	public String createProduct(@ModelAttribute ProductEntity productEntity,
			@RequestParam("imageFile") MultipartFile imageFile) {
		try {
			productService.saveProduct(productEntity, imageFile);
			return "redirect:/products/productList";
		} catch (IOException e) {
			return "error";
		}
	}

	// 관리자용 제품 상세 페이지를 보여줍니다.
	@GetMapping("/productDetail/{id}")
	public String getProductDetail(@PathVariable("id") String id, Model model) {
		Optional<ProductEntity> productEntity = productService.getProductById(id);
		if (productEntity.isPresent()) {
			ProductEntity product = productEntity.get();
			model.addAttribute("product", product);
			model.addAttribute("productThumbnail", product.getProductImagePath());
			return "product/productDetail";
		} else {
			return "error";
		}
	}

	// 회원용 제품 주문 페이지를 보여줍니다.
	@GetMapping("/productOrder/{id}")
	public String getProductOrder(@PathVariable("id") String id, Model model) {
		Optional<ProductEntity> productEntity = productService.getProductById(id);
		if (productEntity.isPresent()) {
			ProductEntity product = productEntity.get();
			model.addAttribute("product", product);
			model.addAttribute("productThumbnail", product.getProductImagePath());
			return "product/productOrder";
		} else {
			return "error";
		}
	}

	// 모든 제품 목록을 보여줍니다.
	@GetMapping("/productList")
	public String getAllProducts(Model model) {
		List<ProductEntity> products = productService.getAllProducts();
		model.addAttribute("products", products);
		return "product/productList";
	}

	// 제품 뷰 페이지를 보여줍니다.
	@GetMapping("/productView")
	public String getProductView(Model model) {
		List<ProductEntity> products = productService.getAllProducts();
		model.addAttribute("products", products);
		model.addAttribute("showFilter", true); // 필터를 표시하도록 설정
		return "product/productView";
	}

	// 제품 수정 폼을 보여줍니다.
	@GetMapping("/update/{id}")
	public String showUpdateForm(@PathVariable("id") String id, Model model) {
		Optional<ProductEntity> productEntity = productService.getProductById(id);
		if (productEntity.isPresent()) {
			model.addAttribute("productEntity", productEntity.get());
			return "product/update";
		} else {
			return "error";
		}
	}

	// 제품 정보를 수정합니다.
	@PostMapping("/update/{id}")
	public String updateProduct(@PathVariable("id") String id, @ModelAttribute ProductEntity productEntity,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
		try {
			productService.updateProduct(id, productEntity, imageFile);
			return "redirect:/products/productList";
		} catch (IOException | IllegalArgumentException e) {
			return "error";
		}
	}

	// 제품 삭제 폼을 보여줍니다.
	@GetMapping("/delete/{id}")
	public String showDeleteForm(@PathVariable("id") String id, Model model) {
		Optional<ProductEntity> productEntity = productService.getProductById(id);
		if (productEntity.isPresent()) {
			model.addAttribute("product", productEntity.get());
			return "product/delete";
		} else {
			return "error";
		}
	}

	// 제품 삭제
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") String id) {
		productService.deleteProduct(id);
		return "redirect:/products/productList";
	}
	//추천
	@GetMapping("/recommendedHtml")
	public String getRecommendedProductsHtml(@RequestParam("productColor") String productColor,
			@RequestParam("productCategory") String productCategory, Model model) {
		List<ProductEntity> filteredProducts = productService.getProductsByColorAndCategory(productColor,
				productCategory);
		model.addAttribute("products", filteredProducts);
		return "product/productView2 :: productList";
	}
	//비교
	@GetMapping("/compare")
	public String compareProducts(HttpSession session, Model model) {
		ProductEntity product1 = (ProductEntity) session.getAttribute("product1");
		ProductEntity product2 = (ProductEntity) session.getAttribute("product2");

		model.addAttribute("product1", product1);
		model.addAttribute("product2", product2);

		return "product/productCompare";
	}
	//비교 추가
	@PostMapping("/compare/add")
	public String addProductToComparePost(@RequestParam("productId") String productId, HttpSession session) {
		Optional<ProductEntity> productOpt = productService.getProductById(productId);
		if (productOpt.isPresent()) {
			ProductEntity product = productOpt.get();
			if (session.getAttribute("product1") == null) {
				session.setAttribute("product1", product);
			} else if (session.getAttribute("product2") == null) {
				session.setAttribute("product2", product);
			}
		}
		return "redirect:/products/compare";
	}

	@GetMapping("/compare/add")
	public String addProductToCompareGet(@RequestParam("productId") String productId, HttpSession session) {
		Optional<ProductEntity> productOpt = productService.getProductById(productId);
		if (productOpt.isPresent()) {
			ProductEntity product = productOpt.get();
			if (session.getAttribute("product1") == null) {
				session.setAttribute("product1", product);
			} else if (session.getAttribute("product2") == null) {
				session.setAttribute("product2", product);
			}
		}
		return "redirect:/products/compare";
	}
	//비교 초기화
	@GetMapping("/compare/reset")
	public String resetProductCompare(HttpSession session) {
		session.removeAttribute("product1");
		session.removeAttribute("product2");
		return "redirect:/products/compare";
	}
	
}
