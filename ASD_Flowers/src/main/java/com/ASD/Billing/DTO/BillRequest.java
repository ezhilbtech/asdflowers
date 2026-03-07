package com.ASD.Billing.DTO;


import java.time.LocalDate;
import java.util.List;

public class BillRequest {

    private String userName;
    private String villageName;
    private String flowerName;

    private LocalDate billDate;   // FIXED

    private double advance;

    private List<BillItemDTO> items;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getFlowerName() {
		return flowerName;
	}

	public void setFlowerName(String flowerName) {
		this.flowerName = flowerName;
	}

	public LocalDate getBillDate() {
		return billDate;
	}

	public void setBillDate(LocalDate billDate) {
		this.billDate = billDate;
	}

	public double getAdvance() {
		return advance;
	}

	public void setAdvance(double advance) {
		this.advance = advance;
	}

	public List<BillItemDTO> getItems() {
		return items;
	}

	public void setItems(List<BillItemDTO> items) {
		this.items = items;
	}

    // getters and setters
    
    
}
