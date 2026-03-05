package com.ASD.Billing.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ASD.Billing.Entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}