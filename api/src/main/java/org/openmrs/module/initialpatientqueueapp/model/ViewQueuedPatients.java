package org.openmrs.module.initialpatientqueueapp.model;

import java.util.Date;

public class ViewQueuedPatients {
	
	private String visitDate;
	
	private String patientIdentifier;
	
	public String getVisitDate() {
		return visitDate;
	}
	
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	
	public String getPatientIdentifier() {
		return patientIdentifier;
	}
	
	public void setPatientIdentifier(String patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}
	
	public String getPatientNames() {
		return patientNames;
	}
	
	public void setPatientNames(String patientNames) {
		this.patientNames = patientNames;
	}
	
	public String getVisitStatus() {
		return visitStatus;
	}
	
	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getServicePoint() {
		return servicePoint;
	}
	
	public void setServicePoint(String servicePoint) {
		this.servicePoint = servicePoint;
	}
	
	private String patientNames;
	
	private String visitStatus;
	
	private String category;
	
	private String servicePoint;
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	private String status;
	
	public Integer getQueueId() {
		return queueId;
	}
	
	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}
	
	private Integer queueId;
	
	private Date birthDate;
	
	private String sex;
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getReferralConceptName() {
		return referralConceptName;
	}
	
	public void setReferralConceptName(String referralConceptName) {
		this.referralConceptName = referralConceptName;
	}
	
	public String getServiceConceptName() {
		return serviceConceptName;
	}
	
	public void setServiceConceptName(String serviceConceptName) {
		this.serviceConceptName = serviceConceptName;
	}
	
	private String referralConceptName;
	
	private String serviceConceptName;
}
