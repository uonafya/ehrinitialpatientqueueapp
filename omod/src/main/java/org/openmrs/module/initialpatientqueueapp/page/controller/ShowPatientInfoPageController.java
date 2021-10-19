package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.BillableService;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@AppPage(InitialPatientQueueConstants.APP_PATIENT_QUEUE)
public class ShowPatientInfoPageController {
	
	private static Log logger = LogFactory.getLog(ShowPatientInfoPageController.class);
	
	public void controller() {
		
	}
	
	public void get(@RequestParam("patientId") Integer patientId,
	        @RequestParam(value = "encounterId", required = false) Integer encounterId,
	        @RequestParam(value = "payCategory", required = false) String payCategory,
	        @RequestParam(value = "roomToVisit", required = false) Integer roomToVisit,
	        @RequestParam(value = "visit", required = false) boolean visit,
	        @RequestParam(value = "departiment", required = false) String departiment, PageModel model) throws IOException,
	        ParseException {
		
		SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy kk:mm");
		model.addAttribute("receiptDate", simpleDate.format(new Date()));
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
		
		User user = Context.getAuthenticatedUser();
		
		model.addAttribute("user", user.getPersonName().getFullName());
		model.addAttribute("names", Context.getPersonService().getPerson(patientId).getPersonName().getFullName());
		model.addAttribute("patientId", Context.getPatientService().getPatient(patientId).getPatientIdentifier()
		        .getIdentifier());
		model.addAttribute("location", Context.getService(KenyaEmrService.class).getDefaultLocation().getName());
		model.addAttribute("age", Context.getPatientService().getPatient(patientId).getAge());
		model.addAttribute("gender", Context.getPatientService().getPatient(patientId).getGender());
		model.addAttribute("previousVisit", hcs.getLastVisitTime(patient));
		String payCat = "";
		boolean paying = false;
		if (payCategory.equals("1")) {
			payCat = "PAYING";
			paying = true;
		} else if (payCategory.equals("2")) {
			payCat = "NON-PAYING";
		} else if (payCategory.equals("3")) {
			payCat = "SPECIAL SCHEMES";
		}
		model.addAttribute("selectedPaymentCategory", payCat);
		model.addAttribute("paying", paying);
		Concept registrationFeesConcept = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_REGISTRATION_FEE);
		Concept revisitFeeConcept = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_REVISIT_FEES);
		Concept specialClinicFeeConcept = Context.getConceptService().getConceptByUuid(
		    InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_CLINIC_FEES);
		
		Concept specialClinicRevisitFeeConcept = Context.getConceptService().getConceptByUuid(
		    InitialPatientQueueConstants.SPECIAL_CLINIC_REVISIT_FEES_UUID);
		
		BillableService registrationFee = Context.getService(BillingService.class).getServiceByConceptId(
		    registrationFeesConcept.getId());
		BillableService revisitFees = Context.getService(BillingService.class).getServiceByConceptId(
		    revisitFeeConcept.getId());
		BillableService specialClinicFeesAmount = Context.getService(BillingService.class).getServiceByConceptId(
		    specialClinicFeeConcept.getId());
		
		BillableService specialClinicRevisitFeesAmount = Context.getService(BillingService.class).getServiceByConceptId(
		    specialClinicRevisitFeeConcept.getId());
		
		Concept mopcTriage = Context.getConceptService().getConceptByUuid("98f596cc-5ad1-4c58-91e8-d1ea0329c89d");
		Concept mopcopd = Context.getConceptService().getConceptByUuid("66710a6d-5894-4f7d-a874-b449df77314d");
		
		Concept mopcRegistartionFess = Context.getConceptService().getConceptByUuid(
		    InitialPatientQueueConstants.MOPC_REGISTARTION_FEE);
		Concept mopcRevisitFeesConcept = Context.getConceptService().getConceptByUuid(
		    InitialPatientQueueConstants.MOPC_REVISIT_FEE);
		
		BillableService mopcRegistrationFee = Context.getService(BillingService.class).getServiceByConceptId(
		    mopcRegistartionFess.getId());
		
		BillableService mopcRevisitFee = Context.getService(BillingService.class).getServiceByConceptId(
		    mopcRevisitFeesConcept.getId());
		
		String WhatToBePaid = "";
		String specialClinicFees = "";
		if (!visit) {
			
			if (Context.getConceptService().getConcept(Integer.parseInt(departiment)).equals(mopcTriage)
			        || Context.getConceptService().getConcept(Integer.parseInt(departiment)).equals(mopcopd)) {
				WhatToBePaid = "Registration fees:		" + mopcRegistrationFee.getPrice();
			} else if (roomToVisit == 3 && !EhrRegistrationUtils.hasSpecialClinicVisit(patient)) {
				WhatToBePaid = "Registration fees:		" + specialClinicFeesAmount.getPrice();
			} else {
				//This a new patient and might be required to pay registration fees
				WhatToBePaid = "Registration fees:		" + registrationFee.getPrice();
			}
		} else {
			
			if (Context.getConceptService().getConcept(Integer.parseInt(departiment)).equals(mopcTriage)
			        || Context.getConceptService().getConcept(Integer.parseInt(departiment)).equals(mopcopd)) {
				WhatToBePaid = "Revisit fees:		" + mopcRevisitFee.getPrice();
			} else if (roomToVisit == 3 && EhrRegistrationUtils.hasSpecialClinicVisit(patient)) {
				WhatToBePaid = "Revisit fees:		" + specialClinicRevisitFeesAmount.getPrice();
			} else {
				//This a new patient and might be required to pay registration fees
				WhatToBePaid = "Revisit fees:		" + revisitFees.getPrice();
			}
			
		}
		
		model.addAttribute("WhatToBePaid", WhatToBePaid);
		model.addAttribute("specialClinicFees", specialClinicFees);
		
		//poppulate the major rooms to visit
		String visitingRoom = "";
		if (roomToVisit != null && roomToVisit == 1) {
			visitingRoom = "Triage Room";
		} else if (roomToVisit != null && roomToVisit == 2) {
			visitingRoom = "OPD Room";
		} else if (roomToVisit != null && roomToVisit == 3) {
			visitingRoom = "Special Clinic";
		}
		model.addAttribute("roomToVisit", visitingRoom);
		model.addAttribute("department", Context.getConceptService().getConcept(Integer.parseInt(departiment)).getName()
		        .getName());
		
	}
	
}
