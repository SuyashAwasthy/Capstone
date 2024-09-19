package com.techlabs.app.dto;

import lombok.Data;

@Data
public class ClaimResponseDto {
	
	private boolean claimedStatus;

	public boolean isClaimedStatus() {
		return claimedStatus;
	}

	public void setClaimedStatus(boolean claimedStatus) {
		this.claimedStatus = claimedStatus;
	}
	
	

}
