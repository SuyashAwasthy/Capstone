package com.techlabs.app.dto;

import lombok.Data;

@Data
public class SubmittedDocumentDto {
private String documentName;
private String documentStatus;
private String documentImage;
public String getDocumentName() {
	return documentName;
}
public void setDocumentName(String documentName) {
	this.documentName = documentName;
}
public String getDocumentStatus() {
	return documentStatus;
}
public void setDocumentStatus(String documentStatus) {
	this.documentStatus = documentStatus;
}
public String getDocumentImage() {
	return documentImage;
}
public void setDocumentImage(String documentImage) {
	this.documentImage = documentImage;
}



}
