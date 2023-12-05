package com.example.autodeal.order.service;

import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.mapper.PaymentDetailsMapper;
import com.example.autodeal.order.model.PaymentDetailsModel;
import com.example.autodeal.order.repository.PaymentDetailsRepository;
import com.example.autodeal.exception.PaymentDetailsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentDetailsService {

    private final PaymentDetailsRepository paymentDetailsRepository;
    private final PaymentDetailsMapper paymentDetailsMapper;

    @Autowired
    public PaymentDetailsService(PaymentDetailsRepository paymentDetailsRepository,
                                 PaymentDetailsMapper paymentDetailsMapper) {
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.paymentDetailsMapper = paymentDetailsMapper;
    }

    @Transactional
    public PaymentDetailsDTO createPaymentDetails(PaymentDetailsDTO paymentDetailsDTO) {
        validatePaymentDetails(paymentDetailsDTO);
        PaymentDetailsModel paymentDetails = paymentDetailsMapper.toPaymentDetailsModel(paymentDetailsDTO);
        PaymentDetailsModel savedPaymentDetails = paymentDetailsRepository.save(paymentDetails);
        return paymentDetailsMapper.toPaymentDetailsDTO(savedPaymentDetails);
    }

    public Optional<PaymentDetailsDTO> getPaymentDetailsById(Integer id) {
        Optional<PaymentDetailsModel> paymentDetails = paymentDetailsRepository.findById(id);
        return paymentDetails.map(paymentDetailsMapper::toPaymentDetailsDTO);
    }

    public List<PaymentDetailsDTO> getAllPaymentDetails() {
        List<PaymentDetailsModel> paymentDetailsList = paymentDetailsRepository.findAll();
        List<PaymentDetailsDTO> dtoList = new ArrayList<>();
        for (PaymentDetailsModel model : paymentDetailsList) {
            dtoList.add(paymentDetailsMapper.toPaymentDetailsDTO(model));
        }
        return dtoList;
    }

    @Transactional
    public PaymentDetailsDTO updatePaymentDetails(PaymentDetailsDTO paymentDetailsDTO) {
        validatePaymentDetails(paymentDetailsDTO);
        if (!paymentDetailsRepository.existsById(paymentDetailsDTO.getId())) {
            throw new PaymentDetailsException("Payment details not found");
        }
        PaymentDetailsModel paymentDetails = paymentDetailsMapper.toPaymentDetailsModel(paymentDetailsDTO);
        PaymentDetailsModel updatedPaymentDetails = paymentDetailsRepository.save(paymentDetails);
        return paymentDetailsMapper.toPaymentDetailsDTO(updatedPaymentDetails);
    }

    public void deletePaymentDetails(Integer id) {
        if (!paymentDetailsRepository.existsById(id)) {
            throw new PaymentDetailsException("Payment details not found");
        }
        paymentDetailsRepository.deleteById(id);
    }

    private void validatePaymentDetails(PaymentDetailsDTO paymentDetailsDTO) {
        if (paymentDetailsDTO.getAmount() == null || paymentDetailsDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentDetailsException("Amount must be greater than 0");
        }
    }
}
