package project.boot.lodeur.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.boot.lodeur.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    Optional<Payment> findByOrder_OrderId(Long orderId);
    
    Optional<Payment> findByMerchantUid(String merchantUid);
}

