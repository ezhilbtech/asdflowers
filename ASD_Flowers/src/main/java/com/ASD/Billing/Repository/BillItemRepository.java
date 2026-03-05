package com.ASD.Billing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ASD.Billing.Entity.BillItem;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {

}