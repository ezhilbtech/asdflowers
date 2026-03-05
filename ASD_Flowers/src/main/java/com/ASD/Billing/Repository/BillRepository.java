package com.ASD.Billing.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ASD.Billing.Entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {

}