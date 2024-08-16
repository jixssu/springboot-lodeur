package project.boot.lodeur.controller;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import project.boot.lodeur.cart.service.CartService;
import project.boot.lodeur.dto.PaymentDTO;
import project.boot.lodeur.entity.PaymentStatus;
import project.boot.lodeur.order.entity.OrderEntity;
import project.boot.lodeur.order.service.OrderService;
import project.boot.lodeur.provider.JwtProvider;
import project.boot.lodeur.service.PaymentService;

@Controller
public class PaymentController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private CartService cartService;

	@GetMapping("/payment/{id}")
	public String showPaymentDetail(@PathVariable Long id, Model model) {
		PaymentDTO paymentDTO = paymentService.getPaymentDTOById(id);
		OrderEntity order = orderService.getOrderById(paymentDTO.getOrder_id());

		model.addAttribute("paymentDTO", paymentDTO);
		model.addAttribute("order", order);

		return "order/orderDetail";
	}

	@GetMapping("/payment/paymentSuccess")
	public String paymentSuccess(@RequestParam(name = "merchant_uid") String merchantUid,
			@RequestParam(name = "order_id") Long orderId, @RequestParam(name = "amount") int amount,
			@RequestParam(name = "paymentstatus") String paymentStatus,
			@RequestParam(name = "payment_date") String paymentDate, HttpServletRequest request, Model model) {

		String token = getTokenFromCookies(request.getCookies());
		String memberId = jwtProvider.validate(token);
		if (memberId == null) {
			return "redirect:/login"; // 또는 적절한 에러 페이지로 리다이렉트
		}

		logger.info("merchant_uid: " + merchantUid);
		logger.info("order_id: " + orderId);
		logger.info("amount: " + amount);
		logger.info("paymentstatus: " + paymentStatus);
		logger.info("payment_date: " + paymentDate);
		logger.info("memberId from JWT: {}", memberId); // 로그 추가

		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setMerchantUid(merchantUid);
		paymentDTO.setOrder_id(orderId);
		paymentDTO.setAmount(amount);
		paymentDTO.setMember_id(memberId); // memberId 추가
		paymentDTO.setPaymentStatus(PaymentStatus.valueOf(paymentStatus));

		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		OffsetDateTime offsetDateTime = OffsetDateTime.parse(paymentDate, formatter);
		LocalDate date = offsetDateTime.toLocalDate();  // LocalDate로 변환
		paymentDTO.setPaymentDate(date);


		logger.info("payment DTO: {}", paymentDTO);

		paymentService.savePayment(paymentDTO, memberId);

		// 주문 상태 업데이트 (수정된 부분)
		OrderEntity order = orderService.getOrderById(orderId);
		order.setPaymentStatus(PaymentStatus.valueOf(paymentStatus));
		orderService.saveOrder(order);

		// 결제 완료 후 장바구니 비우기
		cartService.clearCart(memberId);

		model.addAttribute("merchant_uid", merchantUid);
		model.addAttribute("order_id", orderId);
		model.addAttribute("amount", amount);
		model.addAttribute("paymentstatus", paymentStatus);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedPaymentDate = date.format(outputFormatter);  // date는 LocalDate 객체
		model.addAttribute("formattedPaymentDate", formattedPaymentDate);


		logger.info("모델 추가 payment: " + model); // 로그추가

		return "payment/payment_success";
	}

	private String getTokenFromCookies(Cookie[] cookies) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("jwtToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	@GetMapping("/payments/{orderId}/detail")
	public String getPaymentDetail(@PathVariable("orderId") Long orderId, Model model) {
		OrderEntity order = orderService.getOrderById(orderId);
		model.addAttribute("order", order);
		return "payment/paymentDetail";
	}

	
	@PostMapping("/payment/cancel")
	public ResponseEntity<?> cancelPayment(@RequestParam("merchant_uid") String merchantUid) {
	    try {
	        paymentService.cancelPayment(merchantUid);
	        return ResponseEntity.ok().body("결제 성공");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 실패: " + e.getMessage());
	    }
	}


}
