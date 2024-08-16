package project.boot.lodeur.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import project.boot.lodeur.entity.PaymentStatus;

@Data
public class PaymentDTO {
    private Long id;
    private String merchantUid;
    private Long order_id; // OrderEntity의 ID 값
    private int amount;
    private String member_id; // MemberEntity의 ID 값
    private PaymentStatus paymentStatus;
    private LocalDate paymentDate;
}
