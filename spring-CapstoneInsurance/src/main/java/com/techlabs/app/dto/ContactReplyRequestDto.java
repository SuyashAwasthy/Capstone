package com.techlabs.app.dto;

public class ContactReplyRequestDto {
	private Long contactMessageId;
    private Long agentId;
    private String replyMessage;
	public Long getContactMessageId() {
		return contactMessageId;
	}
	public void setContactMessageId(Long contactMessageId) {
		this.contactMessageId = contactMessageId;
	}
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public String getReplyMessage() {
		return replyMessage;
	}
	public void setReplyMessage(String replyMessage) {
		this.replyMessage = replyMessage;
	}
    
    
}
