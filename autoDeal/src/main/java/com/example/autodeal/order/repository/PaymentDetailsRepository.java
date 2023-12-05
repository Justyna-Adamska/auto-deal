package com.example.autodeal.order.repository;

import com.example.autodeal.order.model.PaymentDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetailsModel, Integer> {

}