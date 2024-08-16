//package project.boot.project.boot.lodeur.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import project.boot.project.boot.lodeur.dto.PaymentDTO;
//import project.boot.project.boot.lodeur.entity.Payment;
//import project.boot.project.boot.lodeur.member.entity.MemberEntity;
//import project.boot.project.boot.lodeur.member.repository.MemberRepository;
//import project.boot.project.boot.lodeur.order.repository.OrderRepository;
//import project.boot.project.boot.lodeur.repository.PaymentRepository;
//
//@Service
//public class PaymentService {
//
//    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    public PaymentDTO getPaymentDTOById(Long paymentId) {
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + paymentId));
//
//        PaymentDTO paymentDTO = new PaymentDTO();
//        paymentDTO.setMerchantUid(payment.getMerchantUid());
//        paymentDTO.setOrder_id(payment.getOrder().getOrderId());
//        paymentDTO.setAmount(payment.getOrderAmount());
//        paymentDTO.setPaymentDate(payment.getPaymentDate());
//        paymentDTO.setMember_id(payment.getMember().getMemberId());
//
//        return paymentDTO;
//    }
//
//    @Transactional
//    public void updatePayment(Long id, PaymentDTO paymentDTO) {
//        Payment payment = paymentRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + id));
//        // payment.setPaymentStatus(paymentDTO.getPaymentStatus());
//        payment.setPaymentDate(paymentDTO.getPaymentDate());
//
//        logger.info("Updating payment with id {}: status = {}, date = {}", id, paymentDTO.getPaymentStatus(), paymentDTO.getPaymentDate());
//
//        paymentRepository.save(payment);
//    }
//
//    @Transactional
//    public void deletePayment(Long id) {
//        Payment payment = paymentRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + id));
//
//        logger.info("Deleting payment with id {}", id);
//
//        paymentRepository.delete(payment);
//    }
//
//    @Transactional
//    public void savePayment(PaymentDTO paymentDTO, String memberId) {
//        Payment payment = new Payment();
//        payment.setMerchantUid(paymentDTO.getMerchantUid());
//        payment.setOrder(orderRepository.findById(paymentDTO.getOrder_id()).orElse(null));
//        payment.setOrderAmount(paymentDTO.getAmount());
//
//        logger.info("Fetching member with id: {}", memberId); // 로그 추가
//        MemberEntity member = memberRepository.findByMemberId(memberId);
//        if (member == null) {
//            throw new IllegalArgumentException("Member not found for id: " + memberId);
//        }
//        payment.setMember(member);
//        payment.setPaymentDate(paymentDTO.getPaymentDate());
//
//        logger.info("Saving payment: merchantUid = {}, status = {}, date = {}, memberId = {}", paymentDTO.getMerchantUid(), paymentDTO.getPaymentStatus(), paymentDTO.getPaymentDate(), member.getId());
//
//        paymentRepository.save(payment);
//    }
//}
package project.boot.lodeur.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;

import project.boot.lodeur.dto.PaymentDTO;
import project.boot.lodeur.entity.Payment;
import project.boot.lodeur.entity.PaymentStatus;
import project.boot.lodeur.member.entity.MemberEntity;
import project.boot.lodeur.member.repository.MemberRepository;
import project.boot.lodeur.order.repository.OrderRepository;
import project.boot.lodeur.repository.PaymentRepository;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private IamportClient iamportClient;

    public PaymentDTO getPaymentDTOById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + paymentId));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantUid(payment.getMerchantUid());
        paymentDTO.setOrder_id(payment.getOrder().getOrderId());
        paymentDTO.setAmount(payment.getOrderAmount());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setMember_id(payment.getMember().getMemberId());

        return paymentDTO;
    }

    @Transactional
    public void updatePayment(Long id, PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + id));
        // payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        payment.setPaymentDate(paymentDTO.getPaymentDate());

        logger.info("Updating payment with id {}: status = {}, date = {}", id, paymentDTO.getPaymentStatus(),
                paymentDTO.getPaymentDate());

        paymentRepository.save(payment);
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + id));

        logger.info("Deleting payment with id {}", id);

        paymentRepository.delete(payment);
    }

    @Transactional
    public void savePayment(PaymentDTO paymentDTO, String memberId) {
        Payment payment = new Payment();
        payment.setMerchantUid(paymentDTO.getMerchantUid());
        payment.setOrder(orderRepository.findById(paymentDTO.getOrder_id()).orElse(null));
        payment.setOrderAmount(paymentDTO.getAmount());

        logger.info("Fetching member with id: {}", memberId); // 로그 추가
        MemberEntity member = memberRepository.findByMemberId(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found for id: " + memberId);
        }
        payment.setMember(member);
        payment.setPaymentDate(paymentDTO.getPaymentDate());

        logger.info("Saving payment: merchantUid = {}, status = {}, date = {}, memberId = {}",
                paymentDTO.getMerchantUid(), paymentDTO.getPaymentStatus(), paymentDTO.getPaymentDate(),
                member.getId());

        paymentRepository.save(payment);
    }

    @Transactional
    public void cancelPayment(String merchantUid) throws IamportResponseException, IOException {
        Payment payment = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for merchant_uid: " + merchantUid));

        CancelData cancelData = new CancelData(merchantUid, true);
        IamportResponse<com.siot.IamportRestClient.response.Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

        if (cancelResponse.getCode() == 0) { // Success
            payment.setPaymentStatus(PaymentStatus.CANCELLED);
            paymentRepository.save(payment);
        } else {
            throw new RuntimeException("Failed to cancel payment: " + cancelResponse.getMessage());
        }
    }
}
