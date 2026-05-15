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
BillRepository billRepository;

@Autowired
CustomerRepository customerRepository;

public Bill saveBill(BillRequest request){

Customer customer=new Customer();

customer.setUserName(request.getUserName());
customer.setVillageName(request.getVillageName());

customer=customerRepository.save(customer);

Bill bill=new Bill();

long count=billRepository.count()+1;

String billNumber="ASD-"+Year.now().getValue()+"-"+String.format("%04d",count);

bill.setBillNumber(billNumber);

bill.setFlowerName(request.getFlowerName());

bill.setBillDate(request.getBillDate());

bill.setCustomer(customer);

double grandTotal=0;
double borrowTotal=0;

List<BillItem> items=new ArrayList<>();

for(BillItemDTO dto:request.getItems()){

BillItem item=new BillItem();

item.setDate(dto.getDate());
item.setQuantity(dto.getQuantity());
item.setRate(dto.getRate());

double total=dto.getQuantity()*dto.getRate();

item.setTotal(Math.round(total));

grandTotal+=total;

if(dto.getBorrow()>0){
borrowTotal+=dto.getBorrow();
}

item.setBorrow(dto.getBorrow());

item.setBill(bill);

items.add(item);
}

bill.setItems(items);

double commission=grandTotal*0.10;

double finalAmount=grandTotal-commission-borrowTotal;

bill.setCommission(commission);
bill.setFinalAmount(finalAmount);

return billRepository.save(bill);

}

public Bill getBillById(Long id){
return billRepository.findById(id).orElseThrow();
}

public void deleteAll(){
billRepository.deleteAll();
}

}
