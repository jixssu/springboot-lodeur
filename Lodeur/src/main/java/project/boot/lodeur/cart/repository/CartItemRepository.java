package project.boot.lodeur.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import project.boot.lodeur.cart.entity.CartItemEntity;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    
    @Transactional
    void deleteByCartMemberId(String memberId);
}
