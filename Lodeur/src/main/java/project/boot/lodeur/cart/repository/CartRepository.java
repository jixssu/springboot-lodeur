package project.boot.lodeur.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.boot.lodeur.cart.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByMemberId(String memberId);
}
