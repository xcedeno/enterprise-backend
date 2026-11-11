package com.xavier.enterprise.repository;

import com.xavier.enterprise.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByApartmentUnit(String apartmentUnit);
    List<Payment> findByStatus(String status);
}

