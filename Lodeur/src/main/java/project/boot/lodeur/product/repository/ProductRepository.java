package project.boot.lodeur.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.boot.lodeur.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
	List<ProductEntity> findByProductColorAndProductCategory(String productColor, String productCategory);
}
