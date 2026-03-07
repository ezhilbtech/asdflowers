package com.ASD.Billing.DTO;

import lombok.Data;

@Data
public class BillItemDTO {

    private String date;
    private double quantity;
    private double rate;
    private double borrow;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getBorrow() {
		return borrow;
	}
	public void setBorrow(double borrow) {
		this.borrow = borrow;
	}
    
    
}
