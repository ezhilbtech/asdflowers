package com.ASD.Billing.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ASD.Billing.DTO.BillRequest;
import com.ASD.Billing.Entity.Bill;
import com.ASD.Billing.Services.BillService;
import com.ASD.Billing.Util.BillPdfService;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin("http://localhost:5173/")
public class BillController {

    @Autowired BillService service;
    @Autowired BillPdfService pdfService;

    @PostMapping
    public Bill save(@RequestBody BillRequest request){
        return service.saveBill(request);
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id){

        Bill bill = service.getBillById(id);

        byte[] pdf = pdfService.generateBillPdf(bill);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename="+bill.getBillNumber()+".pdf")
                .header("Content-Type","application/pdf")
                .body(pdf);
    }
}