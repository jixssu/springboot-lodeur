//package project.boot.project.boot.lodeur.order.entity;
//
//import java.util.List;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import project.boot.project.boot.lodeur.cart.entity.CartEntity;
//import project.boot.project.boot.lodeur.entity.Payment;
//import project.boot.project.boot.lodeur.entity.PaymentStatus;
//import project.boot.project.boot.lodeur.member.entity.MemberEntity;
//
//@Getter
//@Setter
//@ToString
//@Entity
//@Table(name = "orders")
//public class OrderEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "order_id")
//    private Long orderId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cart_id")
//    private CartEntity cart;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "id") 
//    private MemberEntity member;
//
//    @Column(name = "order_amount")
//    private int orderAmount;
//    
//    @Enumerated(EnumType.STRING)
//    private PaymentStatus paymentStatus;
//    
//    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Payment payment;
//    
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<OrderItem> orderItems;
//}
package project.boot.lodeur.order.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.boot.lodeur.cart.entity.CartEntity;
import project.boot.lodeur.entity.Payment;
import project.boot.lodeur.entity.PaymentStatus;
import project.boot.lodeur.member.entity.MemberEntity;

@Getter
@Setter
@ToString
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private MemberEntity member;


    @Column(name = "order_amount")
    private int orderAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentstatus")
    private PaymentStatus paymentStatus;
    
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payment payment;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
