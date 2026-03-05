package com.ASD.Billing.Services;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ASD.Billing.DTO.BillItemDTO;
import com.ASD.Billing.DTO.BillRequest;
import com.ASD.Billing.Entity.Bill;
import com.ASD.Billing.Entity.BillItem;
import com.ASD.Billing.Entity.Customer;
import com.ASD.Billing.Repository.BillRepository;
import com.ASD.Billing.Repository.CustomerRepository;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Bill saveBill(BillRequest request) {

        // Save Customer
        Customer customer = new Customer();
        customer.setUserName(request.getUserName());
        customer.setVillageName(request.getVillageName());
        customer = customerRepository.save(customer);

        // Create Bill
        Bill bill = new Bill();

        // Auto Bill Number
        long count = billRepository.count() + 1;

        String billNumber =
                "ASD-" +
                Year.now().getValue() +
                "-" +
                String.format("%04d", count);

        bill.setBillNumber(billNumber);

        // Flower from frontend
        bill.setFlowerName(request.getFlowerName());

        bill.setBillDate(request.getBillDate());

        bill.setCustomer(customer);

        double grandTotal = 0;

        List<BillItem> items = new ArrayList<>();

        for (BillItemDTO dto : request.getItems()) {

            BillItem item = new BillItem();

            // row date
            item.setDate(dto.getDate());

            item.setQuantity(dto.getQuantity());
            item.setRate(dto.getRate());

            double total =
                    dto.getQuantity() *
                    dto.getRate();

            item.setTotal(total);

            grandTotal += total;

            item.setBill(bill);

            items.add(item);
        }

        bill.setItems(items);

        // Commission
        double commission = grandTotal * 0.10;

        // Final Amount
        double finalAmount =
                grandTotal -
                commission -
                request.getAdvance();

        bill.setCommission(commission);
        bill.setAdvance(request.getAdvance());
        bill.setFinalAmount(finalAmount);

        return billRepository.save(bill);
    }

    public Bill getBillById(Long id) {

        return billRepository
                .findById(id)
                .orElseThrow();
    }
}