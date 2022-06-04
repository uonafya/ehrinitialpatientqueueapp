package org.openmrs.module.initialpatientqueueapp.model;

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

  public String getVisitType() {
    return visitType;
  }

  public void setVisitType(String visitType) {
    this.visitType = visitType;
  }

  public String getRoomToVisit() {
    return roomToVisit;
  }

  public void setRoomToVisit(String roomToVisit) {
    this.roomToVisit = roomToVisit;
  }

  public String getServicePoint() {
    return servicePoint;
  }

  public void setServicePoint(String servicePoint) {
    this.servicePoint = servicePoint;
  }

  private String patientNames;
 private String visitType;
 private String roomToVisit;
 private String servicePoint;
}
