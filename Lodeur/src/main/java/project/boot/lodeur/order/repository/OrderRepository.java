//package project.boot.project.boot.lodeur.order.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import project.boot.project.boot.lodeur.order.entity.OrderEntity;
//
//public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
//    List<OrderEntity> findByMember_MemberId(String memberId);
//}
package project.boot.lodeur.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.boot.lodeur.order.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByMember_Id(Long memberId);  
}
