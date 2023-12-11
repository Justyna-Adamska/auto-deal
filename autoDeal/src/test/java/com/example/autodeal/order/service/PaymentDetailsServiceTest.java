package com.example.autodeal.order.service;

import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.mapper.PaymentDetailsMapper;
import com.example.autodeal.order.model.PaymentDetailsModel;
import com.example.autodeal.order.model.PaymentStatus;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.repository.PaymentDetailsRepository;
import com.example.autodeal.exception.PaymentDetailsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentDetailsServiceTest {

    @Mock
    private PaymentDetailsRepository paymentDetailsRepository;

    @Mock
    private PaymentDetailsMapper paymentDetailsMapper;

    @InjectMocks
    private PaymentDetailsService paymentDetailsService;

    private PaymentDetailsDTO testDto;
    private PaymentDetailsModel testModel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testDto = new PaymentDetailsDTO();
        testDto.setId(1);
        testDto.setAmount(new BigDecimal("10000.00"));
        testDto.setPaymentMethod("DEPOSIT");
        testDto.setReservedAmount(new BigDecimal("2000.00"));
        testDto.setBalanceAmount(new BigDecimal("8000.00"));
        testDto.setPaymentDate(new Date());
        testDto.setReservationExpireDate(new Date());
        testDto.setTransactionId("TRANS123");
        testDto.setStatus("COMPLETED");

        testModel = new PaymentDetailsModel();
        testModel.setId(1);
        testModel.setAmount(new BigDecimal("10000.00"));
        testModel.setPaymentMethod(PaymentType.DEPOSIT);
        testModel.setReservedAmount(new BigDecimal("2000.00"));
        testModel.setBalanceAmount(new BigDecimal("8000.00"));
        testModel.setPaymentDate(LocalDate.of(2026, 12, 10));
        testModel.setReservationExpireDate(LocalDate.of(2026, 12, 10));
        testModel.setTransactionId("TRANS123");
        testModel.setStatus(PaymentStatus.COMPLETED);
    }

    @Test
    public void testCreatePaymentDetails() {
        when(paymentDetailsMapper.toPaymentDetailsModel(testDto)).thenReturn(testModel);
        when(paymentDetailsRepository.save(testModel)).thenReturn(testModel);
        when(paymentDetailsMapper.toPaymentDetailsDTO(testModel)).thenReturn(testDto);

        PaymentDetailsDTO savedDto = paymentDetailsService.createPaymentDetails(testDto);

        assertNotNull(savedDto);
        assertEquals(testDto.getId(), savedDto.getId());
        verify(paymentDetailsRepository).save(testModel);
    }

    @Test
    public void testGetPaymentDetailsById() {
        when(paymentDetailsRepository.findById(testDto.getId())).thenReturn(Optional.of(testModel));
        when(paymentDetailsMapper.toPaymentDetailsDTO(testModel)).thenReturn(testDto);

        Optional<PaymentDetailsDTO> result = paymentDetailsService.getPaymentDetailsById(testDto.getId());

        assertTrue(result.isPresent());
        assertEquals(testDto.getId(), result.get().getId());
    }

    @Test
    public void testGetAllPaymentDetails() {
        when(paymentDetailsRepository.findAll()).thenReturn(Arrays.asList(testModel));
        when(paymentDetailsMapper.toPaymentDetailsDTO(testModel)).thenReturn(testDto);

        List<PaymentDetailsDTO> results = paymentDetailsService.getAllPaymentDetails();

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(testDto.getId(), results.get(0).getId());
    }

    @Test
    public void testUpdatePaymentDetails() {
        when(paymentDetailsRepository.existsById(testDto.getId())).thenReturn(true);
        when(paymentDetailsMapper.toPaymentDetailsModel(testDto)).thenReturn(testModel);
        when(paymentDetailsRepository.save(testModel)).thenReturn(testModel);
        when(paymentDetailsMapper.toPaymentDetailsDTO(testModel)).thenReturn(testDto);

        PaymentDetailsDTO updatedDto = paymentDetailsService.updatePaymentDetails(testDto);

        assertNotNull(updatedDto);
        assertEquals(testDto.getId(), updatedDto.getId());
        verify(paymentDetailsRepository).save(testModel);
    }

    @Test
    public void testDeletePaymentDetails() {
        when(paymentDetailsRepository.existsById(testDto.getId())).thenReturn(true);
        doNothing().when(paymentDetailsRepository).deleteById(testDto.getId());

        paymentDetailsService.deletePaymentDetails(testDto.getId());

        verify(paymentDetailsRepository).deleteById(testDto.getId());
    }

    @Test
    public void testCreatePaymentDetailsWithInvalidData() {
        PaymentDetailsDTO invalidDto = new PaymentDetailsDTO();
        invalidDto.setAmount(BigDecimal.ZERO);

        assertThrows(PaymentDetailsException.class, () -> paymentDetailsService.createPaymentDetails(invalidDto));
    }
    @Test
    public void testGetPaymentDetailsByIdNotFound() {
        Integer id = 999;
        when(paymentDetailsRepository.findById(id)).thenReturn(Optional.empty());

        Optional<PaymentDetailsDTO> result = paymentDetailsService.getPaymentDetailsById(id);

        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdatePaymentDetailsNotFound() {
        PaymentDetailsDTO dtoToUpdate = new PaymentDetailsDTO();
        dtoToUpdate.setId(999);

        when(paymentDetailsRepository.existsById(dtoToUpdate.getId())).thenReturn(false);

        assertThrows(PaymentDetailsException.class, () -> paymentDetailsService.updatePaymentDetails(dtoToUpdate));
    }
    @Test
    public void testGetAllPaymentDetailsEmpty() {
        when(paymentDetailsRepository.findAll()).thenReturn(Collections.emptyList());

        List<PaymentDetailsDTO> results = paymentDetailsService.getAllPaymentDetails();

        assertTrue(results.isEmpty());
    }

    @Test
    public void testCreatePaymentDetailsWithNegativeAmount() {
        PaymentDetailsDTO dtoWithNegativeAmount = new PaymentDetailsDTO();
        dtoWithNegativeAmount.setAmount(new BigDecimal("-100.00"));

        assertThrows(PaymentDetailsException.class, () -> paymentDetailsService.createPaymentDetails(dtoWithNegativeAmount));
    }

    @Test
    public void testUpdatePaymentDetailsWithNullValues() {
        PaymentDetailsDTO dtoWithNullValues = new PaymentDetailsDTO();
        dtoWithNullValues.setId(1);
        dtoWithNullValues.setAmount(null);

        assertThrows(PaymentDetailsException.class, () -> paymentDetailsService.updatePaymentDetails(dtoWithNullValues));
    }

    @Test
    public void testCreatePaymentDetailsRepositoryError() {
        PaymentDetailsDTO validDto = new PaymentDetailsDTO();
        validDto.setAmount(new BigDecimal("100.00"));

        when(paymentDetailsMapper.toPaymentDetailsModel(validDto)).thenReturn(testModel);
        when(paymentDetailsRepository.save(testModel)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> paymentDetailsService.createPaymentDetails(validDto));
    }

}
