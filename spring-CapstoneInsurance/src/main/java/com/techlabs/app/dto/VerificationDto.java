package com.techlabs.app.dto;

import lombok.Data;

@Data
public class VerificationDto {
	private int id;
	
	 private String panCard;
	    private String aadhaarCard;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getPanCard() {
			return panCard;
		}
		public void setPanCard(String panCard) {
			this.panCard = panCard;
		}
		public String getAadhaarCard() {
			return aadhaarCard;
		}
		public void setAadhaarCard(String aadhaarCard) {
			this.aadhaarCard = aadhaarCard;
		}
	    
	    

}
