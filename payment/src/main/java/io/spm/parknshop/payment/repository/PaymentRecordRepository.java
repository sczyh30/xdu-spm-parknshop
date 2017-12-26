package io.spm.parknshop.payment.repository;

import io.spm.parknshop.payment.domain.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {

}
