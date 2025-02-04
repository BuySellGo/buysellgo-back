package com.buysellgo.paymentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.buysellgo.paymentservice.repository.PaymentRepository;
import com.buysellgo.paymentservice.repository.PayMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;   
import com.buysellgo.paymentservice.service.dto.ServiceResult;
import java.util.Map;

import com.buysellgo.paymentservice.common.entity.Role;
import com.buysellgo.paymentservice.controller.dto.PayMethodReq;
import com.buysellgo.paymentservice.controller.dto.PaymentReq;
import com.buysellgo.paymentservice.controller.dto.PaymentStatusReq;
import com.buysellgo.paymentservice.entity.PaymentStatus;
import com.buysellgo.paymentservice.service.dto.PayMethodDto;
import java.util.HashMap;
import static com.buysellgo.paymentservice.common.util.CommonConstant.*;
import com.buysellgo.paymentservice.entity.PayMethod;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import com.buysellgo.paymentservice.entity.Payment;
import com.buysellgo.paymentservice.service.dto.PaymentDto;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PaymentService {
    private final PayMethodRepository payMethodRepository;
    private final PaymentRepository paymentRepository;

    public ServiceResult<Map<String, Object>> registerPayMethod(PayMethodReq req, long userId){
        Map<String, Object> data = new HashMap<>();
        try{
            PayMethodDto payMethodDto = PayMethodDto.from(req, userId);
            PayMethod payMethod=payMethodRepository.save(PayMethod.of(payMethodDto.getUserId(), payMethodDto.getPayMethodName(), payMethodDto.getPayMethodType(), payMethodDto.getPayMethodNumber()));
            data.put(PAYMENT_METHOD_VO.getValue(), payMethod.toVo());
            return ServiceResult.success(PAYMENT_METHOD_REGISTER_SUCCESS.getValue(),data);
        } catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(PAYMENT_METHOD_REGISTER_FAIL.getValue(),data);
        }
    }


    public ServiceResult<Map<String, Object>> getPayMethodList(long userId){
        Map<String, Object> data = new HashMap<>();
        try{
            List<PayMethod> payMethods = payMethodRepository.findAllByUserId(userId);
            data.put(PAYMENT_METHOD_VO.getValue(), payMethods.stream().map(PayMethod::toVo).toList());
            return ServiceResult.success(PAYMENT_METHOD_LIST_SUCCESS.getValue(),data);
        } catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(PAYMENT_METHOD_LIST_FAIL.getValue(),data);
        }
    }


    public ServiceResult<Map<String, Object>> updatePayMethod(PayMethodReq req, long userId, long payMethodId){
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<PayMethod> payMethodOptional = payMethodRepository.findById(payMethodId);
            if(payMethodOptional.isEmpty()){
                return ServiceResult.fail(PAYMENT_METHOD_NOT_FOUND.getValue(),data);
            }
            if(payMethodOptional.get().getUserId() != userId){
                return ServiceResult.fail(PAYMENT_METHOD_UPDATE_PERMISSION_DENIED.getValue(),data);
            }
            PayMethod payMethod = payMethodOptional.get();
            PayMethodDto payMethodDto = PayMethodDto.from(req, userId);
            payMethod.setPayMethodName(payMethodDto.getPayMethodName());
            payMethod.setPayMethodType(payMethodDto.getPayMethodType());
            payMethod.setPayMethodNumber(payMethodDto.getPayMethodNumber());
            payMethodRepository.save(payMethod);
            data.put(PAYMENT_METHOD_VO.getValue(), payMethod.toVo());
            return ServiceResult.success(PAYMENT_METHOD_UPDATE_SUCCESS.getValue(),data);
        } catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(PAYMENT_METHOD_UPDATE_FAIL.getValue(),data);
        }
    }



    public ServiceResult<Map<String, Object>> deletePayMethod(long payMethodId, long userId){
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<PayMethod> payMethodOptional = payMethodRepository.findById(payMethodId);
            if(payMethodOptional.isEmpty()){
                return ServiceResult.fail(PAYMENT_METHOD_NOT_FOUND.getValue(),data);
            }   
            if(payMethodOptional.get().getUserId() != userId){
                return ServiceResult.fail(PAYMENT_METHOD_DELETE_PERMISSION_DENIED.getValue(),data);
            }
            payMethodRepository.delete(payMethodOptional.get());
            data.put(PAYMENT_METHOD_VO.getValue(), payMethodOptional.get().toVo());
            return ServiceResult.success(PAYMENT_METHOD_DELETE_SUCCESS.getValue(),data);
        } catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(PAYMENT_METHOD_DELETE_FAIL.getValue(),data);
        }
    }


    public ServiceResult<Map<String, Object>> createPayment(PaymentReq req, long userId){
        Map<String, Object> data = new HashMap<>();
        try{
            PaymentDto paymentDto = PaymentDto.from(req, userId);
            Optional<PayMethod> payMethodOptional = payMethodRepository.findByPayMethodName(paymentDto.getPaymentMethod());
            if(payMethodOptional.isEmpty()){
                return ServiceResult.fail(PAYMENT_METHOD_NOT_FOUND.getValue(),data);
            }
            Optional<Payment> paymentOptional = paymentRepository.findByOrderId(paymentDto.getOrderId());
            if(paymentOptional.isPresent()){
                return ServiceResult.fail(PAYMENT_ALREADY_EXISTS.getValue(),data);
            }
            Payment payment = paymentRepository.save(Payment.of(paymentDto.getOrderId(), paymentDto.getUserId(), paymentDto.getProductId(), paymentDto.getTotalPrice(), paymentDto.getPaymentMethod(), paymentDto.getGroupId()));
            data.put(PAYMENT_VO.getValue(), payment.toVo());


            //상품서비스에 수량 변경 요청
            //주문서비스에 주문 상태 변경 요청
            return ServiceResult.success(PAYMENT_CREATE_SUCCESS.getValue(),data);
        } catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(PAYMENT_CREATE_FAIL.getValue(),data);
        }

    }

    public ServiceResult<Map<String, Object>> getPaymentHistory(long userId, Role role){
        Map<String, Object> data = new HashMap<>();
        try{
            if(role == Role.ADMIN){
                List<Payment> payments = paymentRepository.findAll();
                data.put(PAYMENT_VO.getValue(), payments.stream().map(Payment::toVo).toList());
            }else{
                List<Payment> payments = paymentRepository.findAllByUserId(userId);
                data.put(PAYMENT_VO.getValue(), payments.stream().map(Payment::toVo).toList());
            }
            return ServiceResult.success(PAYMENT_HISTORY_SUCCESS.getValue(),data);
        } catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(PAYMENT_HISTORY_FAIL.getValue(),data);
        }
    }

    public ServiceResult<Map<String, Object>> updatePaymentStatus(PaymentStatusReq req){    
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Payment> paymentOptional = paymentRepository.findById(req.paymentId());
            if(paymentOptional.isEmpty()){
                return ServiceResult.fail(PAYMENT_NOT_FOUND.getValue(),data);
            }
            Payment payment = paymentOptional.get();
            payment.setPaymentStatus(req.status());
            paymentRepository.save(payment);
            data.put(PAYMENT_VO.getValue(), payment.toVo());
            return ServiceResult.success(PAYMENT_STATUS_UPDATE_SUCCESS.getValue(),data);

        } catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(PAYMENT_STATUS_UPDATE_FAIL.getValue(),data);
        }
    }
}
