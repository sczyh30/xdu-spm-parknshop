package io.spm.parknshop.payment.repository;

import io.spm.parknshop.payment.domain.TransferTransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferTransactionRepository extends JpaRepository<TransferTransactionRecord, Long> {

}
