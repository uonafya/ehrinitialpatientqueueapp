/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Visit;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ehrconfigs.metadata.EhrCommonMetadata;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.model.BillableService;
import org.openmrs.module.hospitalcore.model.DepartmentConcept;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.initialpatientqueueapp.includable.validator.attribute.PatientAttributeValidatorService;
import org.openmrs.module.initialpatientqueueapp.web.controller.utils.RegistrationWebUtils;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 4 Fragment to process the queueing information for a patient return processed patients
 */
public class QueuePatientFragmentController {
	
	private static Log logger = LogFactory.getLog(QueuePatientFragmentController.class);
	
	public void controller(@FragmentParam("patient") Patient patient, FragmentModel model) throws ParseException {
		
		model.addAttribute("TRIAGE", RegistrationWebUtils.getSubConcepts(InitialPatientQueueConstants.CONCEPT_NAME_TRIAGE));
		model.addAttribute("OPDs", RegistrationWebUtils.getSubConcepts(InitialPatientQueueConstants.CONCEPT_NAME_OPD_WARD));
		model.addAttribute("SPECIALCLINIC",
		    RegistrationWebUtils.getSubConcepts(InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_CLINIC));
		model.addAttribute("payingCategory",
		    RegistrationWebUtils.getSubConceptsWithName(InitialPatientQueueConstants.CONCEPT_NAME_PAYING_CATEGORY));
		model.addAttribute("nonPayingCategory",
		    RegistrationWebUtils.getUniqueSubConceptsWithName(InitialPatientQueueConstants.CONCEPT_NAME_NONPAYING_CATEGORY));
		model.addAttribute("specialScheme",
		    RegistrationWebUtils.getSubConceptsWithName(InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_SCHEME));
		model.addAttribute("universities",
		    RegistrationWebUtils.getSubConceptsWithName(InitialPatientQueueConstants.CONCEPT_NAME_LIST_OF_UNIVERSITIES));
		model.addAttribute("age", patient.getAge());
		
		Map<Integer, String> payingCategoryMap = new LinkedHashMap<Integer, String>();
		Concept payingCategory = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_PAYING_CATEGORY);
		for (ConceptAnswer ca : payingCategory.getAnswers()) {
			payingCategoryMap.put(ca.getAnswerConcept().getConceptId(), ca.getAnswerConcept().getName().getName());
		}
		Map<Integer, String> nonPayingCategoryMap = new LinkedHashMap<Integer, String>();
		Concept nonPayingCategory = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_NONPAYING_CATEGORY);
		for (ConceptAnswer ca : nonPayingCategory.getAnswers()) {
			if (!nonPayingCategoryMap.containsKey(ca.getAnswerConcept().getConceptId())) {
				nonPayingCategoryMap.put(ca.getAnswerConcept().getConceptId(), ca.getAnswerConcept().getName().getName());
			}
			
		}
		Map<Integer, String> specialSchemeMap = new LinkedHashMap<Integer, String>();
		Concept specialScheme = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_SCHEME);
		for (ConceptAnswer ca : specialScheme.getAnswers()) {
			specialSchemeMap.put(ca.getAnswerConcept().getConceptId(), ca.getAnswerConcept().getName().getName());
		}
		model.addAttribute("payingCategoryMap", payingCategoryMap);
		model.addAttribute("nonPayingCategoryMap", nonPayingCategoryMap);
		model.addAttribute("specialSchemeMap", specialSchemeMap);
		model.addAttribute("initialRegFee",
		    GlobalPropertyUtil.getString(InitialPatientQueueConstants.PROPERTY_INITIAL_REGISTRATION_FEE, ""));
		model.addAttribute("visitType", hasRevisits(patient));
		model.addAttribute("childLessThanFiveYearRegistrationFee",
		    GlobalPropertyUtil.getString(InitialPatientQueueConstants.PROPERTY_CHILDLESSTHANFIVEYEAR_REGISTRATION_FEE, ""));
		model.addAttribute("specialClinicRegFee",
		    GlobalPropertyUtil.getString(InitialPatientQueueConstants.PROPERTY_SPECIALCLINIC_REGISTRATION_FEE, ""));
		KenyaEmrService kenyaEmrService = Context.getService(KenyaEmrService.class);
		model.addAttribute("userLocation", kenyaEmrService.getDefaultLocation().getName());
		model.addAttribute("receiptDate", new Date());
		
	}
	
	public String post(HttpServletRequest request, PageModel model, UiUtils uiUtils, HttpServletResponse response,
	        @RequestParam("patientId") Patient patient, @RequestParam("paym_1") String paymentCategory) throws IOException,
	        ParseException {
		
		Map<String, String> parameters = RegistrationWebUtils.optimizeParameters(request);
		int roomToVisit = Integer.parseInt(parameters.get("rooms1"));
		int payCat = Integer.parseInt(paymentCategory);
		
		Map<String, Object> redirectParams = new HashMap<String, Object>();
		Map<Integer, String> payingCategoryMap = new LinkedHashMap<Integer, String>();
		Concept payingCategory = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_PAYING_CATEGORY);
		for (ConceptAnswer ca : payingCategory.getAnswers()) {
			payingCategoryMap.put(ca.getAnswerConcept().getConceptId(), ca.getAnswerConcept().getName().getName());
		}
		Map<Integer, String> nonPayingCategoryMap = new LinkedHashMap<Integer, String>();
		Concept nonPayingCategory = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_NONPAYING_CATEGORY);
		for (ConceptAnswer ca : nonPayingCategory.getAnswers()) {
			nonPayingCategoryMap.put(ca.getAnswerConcept().getConceptId(), ca.getAnswerConcept().getName().getName());
		}
		Map<Integer, String> specialSchemeMap = new LinkedHashMap<Integer, String>();
		Concept specialScheme = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_SCHEME);
		for (ConceptAnswer ca : specialScheme.getAnswers()) {
			specialSchemeMap.put(ca.getAnswerConcept().getConceptId(), ca.getAnswerConcept().getName().getName());
		}
		model.addAttribute("payingCategoryMap", payingCategoryMap);
		model.addAttribute("nonPayingCategoryMap", nonPayingCategoryMap);
		model.addAttribute("specialSchemeMap", specialSchemeMap);
		model.addAttribute("TRIAGE", RegistrationWebUtils.getSubConcepts(InitialPatientQueueConstants.CONCEPT_NAME_TRIAGE));
		model.addAttribute("OPDs", RegistrationWebUtils.getSubConcepts(InitialPatientQueueConstants.CONCEPT_NAME_OPD_WARD));
		model.addAttribute("SPECIALCLINIC",
		    RegistrationWebUtils.getSubConcepts(InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_CLINIC));
		model.addAttribute("payingCategory",
		    RegistrationWebUtils.getSubConceptsWithName(InitialPatientQueueConstants.CONCEPT_NAME_PAYING_CATEGORY));
		model.addAttribute("nonPayingCategory",
		    RegistrationWebUtils.getSubConceptsWithName(InitialPatientQueueConstants.CONCEPT_NAME_NONPAYING_CATEGORY));
		model.addAttribute("specialScheme",
		    RegistrationWebUtils.getSubConceptsWithName(InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_SCHEME));
		model.addAttribute("universities",
		    RegistrationWebUtils.getSubConceptsWithName(InitialPatientQueueConstants.CONCEPT_NAME_LIST_OF_UNIVERSITIES));
		model.addAttribute("initialRegFee",
		    GlobalPropertyUtil.getString(InitialPatientQueueConstants.PROPERTY_INITIAL_REGISTRATION_FEE, ""));
		model.addAttribute("childLessThanFiveYearRegistrationFee",
		    GlobalPropertyUtil.getString(InitialPatientQueueConstants.PROPERTY_CHILDLESSTHANFIVEYEAR_REGISTRATION_FEE, ""));
		model.addAttribute("specialClinicRegFee",
		    GlobalPropertyUtil.getString(InitialPatientQueueConstants.PROPERTY_SPECIALCLINIC_REGISTRATION_FEE, ""));
		KenyaEmrService kenyaEmrService = Context.getService(KenyaEmrService.class);
		List<Visit> patientVisit = Context.getVisitService().getVisits(
		    Arrays.asList(Context.getVisitService().getVisitTypeByUuid("3371a4d4-f66f-4454-a86d-92c7b3da990c")),
		    Arrays.asList(patient), Arrays.asList(kenyaEmrService.getDefaultLocation()), null, null, null, null, null, null,
		    false, false);
		model.addAttribute("userLocation", kenyaEmrService.getDefaultLocation().getName());
		model.addAttribute("receiptDate", new Date());
		consolidateAllPersonalAttributes(parameters, patient);
		try {
			//create a patient search here
			savePatientSearch(patient);
			// create encounter for the visit here
			Encounter encounter = createEncounter(patient, parameters);
			hasActiveVisit(patientVisit, patient, encounter);
			//encounter = Context.getEncounterService().saveEncounter(encounter);
			//encounter.setVisit(getLastVisitForPatient(patient));
			//try sending this patient as an opd test order such that it can be seen at the billing point
			sendToBillingDependingOnTheBill(parameters, encounter, payCat);
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("success");
			redirectParams.put("status", "success");
			redirectParams.put("patientId", patient.getPatientId());
			redirectParams.put("encounterId", encounter.getId());
			//ADD PERSON ATTRIBUTE SET
			model.addAttribute("status", "success");
			model.addAttribute("patientId", patient.getPatientId());
			model.addAttribute("encounterId", encounter.getId());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", "error");
			model.addAttribute("message", e.getMessage());
		}
		return "redirect:"
		        + uiUtils.pageLink("initialpatientqueueapp", "showPatientInfo?patientId=" + patient.getPatientId()
		                + "&visit=" + hasRevisits(patient) + "&payCategory=" + paymentCategory + "&roomToVisit="
		                + roomToVisit);
	}
	
	/**
	 * Create Encounter For The Visit Of Patient
	 * 
	 * @param patient
	 * @param parameters
	 * @return
	 */
	private Encounter createEncounter(Patient patient, Map<String, String> parameters) throws ParseException {
		int rooms1 = Integer.parseInt(parameters.get("rooms1"));
		int paymt1 = Integer.parseInt(parameters.get("paym_1"));
		int paymt2 = Integer.parseInt(parameters.get("paym_2"));
		int status = Integer.parseInt(parameters.get("visitType"));
		
		String paymt3 = null;
		String paymt4 = null;
		
		String tNTriage = null, oNOpd = null, sNSpecial = null, nFNumber;
		String nPayn = null, nNotpayn = null, nScheme = null, nNHIFnumb = null, nWaivernumb = null, nUniID = null, nStuID = null;
		
		switch (rooms1) {
			case 1: {
				tNTriage = parameters.get("rooms2");
				break;
			}
			case 2: {
				oNOpd = parameters.get("rooms2");
				break;
			}
			case 3: {
				sNSpecial = parameters.get("rooms2");
				nFNumber = parameters.get("rooms3");
				break;
			}
		}
		
		switch (paymt1) {
			case 1: {
				paymt3 = "Paying";
				if (paymt2 == 1) {
					nPayn = "Special clinic";
				} else if (paymt2 == 2) {
					nPayn = "General patient";
				}
				
				break;
			}
			case 2: {
				paymt3 = "Non-Paying";
				
				if (paymt2 == 1) {
					nNotpayn = "NHIF CIVIL SERVANT";
					nNHIFnumb = parameters.get("modesummary");
				} else if (paymt2 == 2) {
					nNotpayn = "CCC PATIENT";
				} else if (paymt2 == 3) {
					nNotpayn = "TB PATIENT";
				} else if (paymt2 == 4) {
					nNotpayn = "PRISIONER";
				} else if (paymt2 == 6) {
					nNotpayn = "NHIF PATIENT";
					nNHIFnumb = parameters.get("modesummary");
				}
				break;
			}
			case 3: {
				paymt3 = "Special Schemes";
				
				if (paymt2 == 1) {
					nWaivernumb = parameters.get("modesummary");
					nScheme = "WAIVER CASE";
				} else if (paymt2 == 2) {
					nScheme = "DELIVERY CASE";
				} else if (paymt2 == 3) {
					nUniID = parameters.get("university");
					nStuID = parameters.get("modesummary");
					nScheme = "STUDENT SCHEME";
				}
				if (rooms1 == 1) {
					nFNumber = parameters.get("rooms3");
				}
				break;
			}
		}
		
		Encounter encounter = RegistrationWebUtils.createEncounter(patient, hasRevisits(patient));
		
		if (!StringUtils.isBlank(tNTriage)) {
			
			Concept triageConcept = Context.getConceptService().getConceptByUuid("e8acf3d5-d451-475b-a3b5-37f0ce6a0260");
			
			Concept selectedTRIAGEConcept = Context.getConceptService().getConcept(tNTriage);
			
			String selectedCategory = paymt3;
			Obs triageObs = new Obs();
			triageObs.setConcept(triageConcept);
			triageObs.setValueCoded(selectedTRIAGEConcept);
			encounter.addObs(triageObs);
			RegistrationWebUtils.sendPatientToTriageQueue(patient, selectedTRIAGEConcept, hasRevisits(patient),
			    selectedCategory);
		} else if (!StringUtils.isBlank(oNOpd)) {
			Concept opdConcept = Context.getConceptService().getConceptByUuid("03880388-07ce-4961-abe7-0e58f787dd23");
			Concept selectedOPDConcept = Context.getConceptService().getConcept(oNOpd);
			String selectedCategory = paymt3;
			Obs opdObs = new Obs();
			opdObs.setConcept(opdConcept);
			opdObs.setValueCoded(selectedOPDConcept);
			encounter.addObs(opdObs);
			RegistrationWebUtils.sendPatientToOPDQueue(patient, selectedOPDConcept, hasRevisits(patient), selectedCategory);
			
		} else {
			Concept specialClinicConcept = Context.getConceptService().getConceptByUuid(
			    "b5e0cfd3-1009-4527-8e36-83b5e902b3ea");
			Concept selectedSpecialClinicConcept = Context.getConceptService().getConcept(sNSpecial);
			String selectedCategory = paymt3;
			Obs opdObs = new Obs();
			opdObs.setConcept(specialClinicConcept);
			opdObs.setValueCoded(selectedSpecialClinicConcept);
			encounter.addObs(opdObs);
			
			RegistrationWebUtils.sendPatientToOPDQueue(patient, selectedSpecialClinicConcept, hasRevisits(patient),
			    selectedCategory);
			
		}
		
		// payment category and registration fee
		Concept cnrf = Context.getConceptService().getConcept(InitialPatientQueueConstants.CONCEPT_NAME_REGISTRATION_FEE);
		Concept revisitFeeConcept = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_REVISIT_FEES);
		Concept specialClinicFeeConcept = Context.getConceptService().getConceptByUuid(
		    InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_CLINIC_FEES);
		
		Concept cnp = Context.getConceptService().getConcept(InitialPatientQueueConstants.CONCEPT_NEW_PATIENT);
		Concept crp = Context.getConceptService().getConcept(InitialPatientQueueConstants.CONCEPT_REVISIT);
		Concept csc = Context.getConceptService().getConcept(InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_CLINIC);
		
		Obs obsn = new Obs();
		if (!hasRevisits(patient)) {
			obsn.setConcept(cnrf);
			obsn.setValueCoded(cnp);
			double doubleVal = Double.parseDouble(GlobalPropertyUtil.getString(
			    InitialPatientQueueConstants.FORM_FIELD_REGISTRATION_FEE, "0.0"));
			obsn.setValueNumeric(doubleVal);
			
		} else {
			obsn.setConcept(revisitFeeConcept);
			obsn.setValueCoded(crp);
			double doubleVal = Double.parseDouble(GlobalPropertyUtil.getString(
			    InitialPatientQueueConstants.PROPERTY_REVISIT_REGISTRATION_FEE, "0.0"));
			obsn.setValueNumeric(doubleVal);
		}
		
		if (StringUtils.isNotEmpty(sNSpecial)) {
			obsn.setConcept(specialClinicFeeConcept);
			obsn.setValueCoded(csc);
			double doubleVal = Double.parseDouble(GlobalPropertyUtil.getString(
			    InitialPatientQueueConstants.PROPERTY_SPECIALCLINIC_REGISTRATION_FEE, "0.0"));
			obsn.setValueNumeric(doubleVal);
		}
		
		obsn.setValueText(paymt3 + "/" + paymt2);//we can add the paying sub category if needed
		
		if (paymt3 != null && paymt3.equals("Paying")) {
			obsn.setComment(nPayn);
		} else if (paymt3 != null && paymt3.equals("Non-Paying")) {
			obsn.setComment(nNotpayn);
		} else {
			obsn.setComment(nScheme);
		}
		encounter.addObs(obsn);
		return encounter;
	}
	
	private boolean hasRevisits(Patient patient) throws ParseException {
		boolean found = false;
		KenyaEmrService kenyaEmrService = Context.getService(KenyaEmrService.class);
		EncounterType patientQueueEncounter = Context.getEncounterService().getEncounterTypeByUuid(
		    "356d447a-b494-11ea-8337-f7bcaf3e8fec");
		EncounterType triageEncounter = Context.getEncounterService().getEncounterTypeByUuid(
		    "2af60550-f291-11ea-b725-9753b5f685ae");
		EncounterType opdEncounter = Context.getEncounterService().getEncounterTypeByUuid(
		    "ba45c278-f290-11ea-9666-1b3e6e848887");
		EncounterType registrationInitial = Context.getEncounterService().getEncounterTypeByUuid(
		    "8efa1534-f28f-11ea-b25f-af56118cf21b");
		EncounterType revisitInitial = Context.getEncounterService().getEncounterTypeByUuid(
		    "98d42234-f28f-11ea-b609-bbd062a0383b");
		List<Encounter> filteredVisits = Context.getEncounterService().getEncounters(patient,
		    kenyaEmrService.getDefaultLocation(), null, null, null,
		    Arrays.asList(patientQueueEncounter, triageEncounter, opdEncounter, registrationInitial, revisitInitial), null,
		    null, null, false);
		
		if (filteredVisits.size() > 0) {
			Encounter encounterVisit = filteredVisits.get(0);
			if (EhrRegistrationUtils.parseDate(EhrRegistrationUtils.formatDate(encounterVisit.getEncounterDatetime()))
			        .before(EhrRegistrationUtils.parseDate(EhrRegistrationUtils.formatDate(new Date())))) {
				found = true;
			}
		}
		return found;
	}
	
	private void hasActiveVisit(List<Visit> visits, Patient patient, Encounter encounter) {
		VisitService visitService = Context.getVisitService();
		KenyaEmrService kenyaEmrService = Context.getService(KenyaEmrService.class);
		if (visits.size() > 0 && visits.get(0).getStopDatetime() == null) {
			visits.get(0).addEncounter(encounter);
		} else {
			Visit visit = new Visit();
			visit.addEncounter(encounter);
			visit.setPatient(patient);
			visit.setVisitType(visitService.getVisitTypeByUuid("3371a4d4-f66f-4454-a86d-92c7b3da990c"));
			visit.setStartDatetime(getAminuteBefore());
			visit.setLocation(kenyaEmrService.getDefaultLocation());
			visit.setCreator(Context.getAuthenticatedUser());
			visitService.saveVisit(visit);
		}
	}
	
	private Person setAttributes(Patient patient, Map<String, String> attributes) throws Exception {
		PatientAttributeValidatorService validator = new PatientAttributeValidatorService();
		Map<String, Object> parameters = HospitalCoreUtils.buildParameters("patient", patient, "attributes", attributes);
		String validateResult = validator.validate(parameters);
		logger.info("Attirubte validation: " + validateResult);
		if (StringUtils.isBlank(validateResult)) {
			for (String name : attributes.keySet()) {
				if ((name.contains(".attribute.")) && (!StringUtils.isBlank(attributes.get(name)))) {
					String[] parts = name.split("\\.");
					String idText = parts[parts.length - 1];
					Integer id = Integer.parseInt(idText);
					PersonAttribute attribute = EhrRegistrationUtils.getPersonAttribute(id, attributes.get(name));
					patient.addAttribute(attribute);
				}
			}
		} else {
			throw new Exception(validateResult);
		}
		
		return patient;
	}
	
	private void getPatientCategoryPersonAttributesPerTheSession(Map<String, String> attributes, Patient patient) {
		
		int paymt1 = Integer.parseInt(attributes.get("paym_1"));
		
		PersonAttributeType paymentCategoryPaymentAttribute = Context.getPersonService().getPersonAttributeTypeByUuid(
		    EhrCommonMetadata._EhrPersonAttributeType.PAYMENT_CATEGORY);
		
		PersonAttribute checkIfExists = patient.getAttribute(paymentCategoryPaymentAttribute);
		//set value to be used
		String valueParam1 = "";
		if (paymt1 == 1) {
			valueParam1 = "Paying";
		} else if (paymt1 == 2) {
			valueParam1 = "Non paying";
		} else if (paymt1 == 3) {
			valueParam1 = "Special scheme";
		}
		//set up the person attribute for the payment category
		if (checkIfExists == null) {
			checkIfExists = new PersonAttribute();
			checkIfExists.setAttributeType(paymentCategoryPaymentAttribute);
			checkIfExists.setCreator(Context.getAuthenticatedUser());
			checkIfExists.setDateCreated(new Date());
			checkIfExists.setPerson(patient);
		}
		checkIfExists.setValue(valueParam1);
		patient.addAttribute(checkIfExists);
		
	}
	
	private void getPayingCategoryPersonAttribute(Map<String, String> attributes, Patient patient) {
		int paymt1 = Integer.parseInt(attributes.get("paym_1"));
		int paymt2 = Integer.parseInt(attributes.get("paym_2"));
		String param2Value = "";
		
		if (paymt1 == 1) {
			if (paymt2 == 1) {
				param2Value = "Special clinic";
			} else if (paymt2 == 2) {
				param2Value = "General patient";
			}
		} else if (paymt1 == 2) {
			if (paymt2 == 1) {
				param2Value = "Child under 5";
			} else if (paymt2 == 2 || paymt2 == 3) {
				param2Value = "Currently pregnant";
			} else if (paymt2 == 4 || paymt2 == 5) {
				param2Value = "CCC patient";
			} else if (paymt2 == 6 || paymt2 == 7) {
				param2Value = "TB patient";
			} else if (paymt2 == 8) {
				param2Value = "Patient in prison";
			} else if (paymt2 == 9) {
				param2Value = "NHIF patient";
			} else if (paymt2 == 10) {
				param2Value = "Civil servant";
			}
		} else if (paymt1 == 3) {
			if (paymt2 == 1) {
				param2Value = "Waiver";
			} else if (paymt2 == 2) {
				param2Value = "Delivery case";
			} else if (paymt2 == 3) {
				param2Value = "Student";
			}
		}
		PersonAttributeType paymentCategorySubTypePaymentAttribute = Context.getPersonService()
		        .getPersonAttributeTypeByUuid(EhrCommonMetadata._EhrPersonAttributeType.PAYMENT_CATEGORY_SUB_TYPE);
		PersonAttribute checkIfParam2Exists = patient.getAttribute(paymentCategorySubTypePaymentAttribute);
		if (checkIfParam2Exists == null) {
			checkIfParam2Exists = new PersonAttribute();
			checkIfParam2Exists.setAttributeType(paymentCategorySubTypePaymentAttribute);
			checkIfParam2Exists.setCreator(Context.getAuthenticatedUser());
			checkIfParam2Exists.setDateCreated(new Date());
			checkIfParam2Exists.setPerson(patient);
		}
		checkIfParam2Exists.setValue(param2Value);
		patient.addAttribute(checkIfParam2Exists);
	}
	
	private void getFileNumberPersonAttribute(Map<String, String> attributes, Patient patient) {
		String fileNumber = attributes.get("rooms3");
		PersonAttributeType fileNumberAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(
		    EhrCommonMetadata._EhrPersonAttributeType.FILE_NUMBER);
		PersonAttribute fileNumberAttribute = patient.getAttribute(fileNumberAttributeType);
		
		if (!fileNumber.isEmpty()) {
			if (fileNumberAttribute == null) {
				fileNumberAttribute = new PersonAttribute();
				fileNumberAttribute.setAttributeType(fileNumberAttributeType);
				fileNumberAttribute.setCreator(Context.getAuthenticatedUser());
				fileNumberAttribute.setDateCreated(new Date());
				fileNumberAttribute.setPerson(patient);
				
			}
			fileNumberAttribute.setValue(fileNumber);
			patient.addAttribute(fileNumberAttribute);
		}
		
	}
	
	private void getUniversityPersonAttribute(Map<String, String> attributes, Patient patient) {
		String university = attributes.get("university");
		PersonAttributeType universityAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(
		    EhrCommonMetadata._EhrPersonAttributeType.UNIVERSITY);
		PersonAttribute universityAttribute = patient.getAttribute(universityAttributeType);
		if (!university.isEmpty()) {
			if (universityAttribute == null) {
				universityAttribute = new PersonAttribute();
				universityAttribute.setAttributeType(universityAttributeType);
				universityAttribute.setCreator(Context.getAuthenticatedUser());
				universityAttribute.setDateCreated(new Date());
				universityAttribute.setPerson(patient);
				
			}
			universityAttribute.setValue(university);
			patient.addAttribute(universityAttribute);
		}
		
	}
	
	private void getNhifNumberPersonAttribute(Map<String, String> attributes, Patient patient) {
		String nhifNumber = attributes.get("nhifNumber");
		PersonAttributeType nhifNumberAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(
		    EhrCommonMetadata._EhrPersonAttributeType.NHIF_CARD_NUMBER);
		PersonAttribute nhifNumberAttribute = patient.getAttribute(nhifNumberAttributeType);
		if (!nhifNumber.isEmpty()) {
			if (nhifNumberAttribute == null) {
				nhifNumberAttribute = new PersonAttribute();
				nhifNumberAttribute.setAttributeType(nhifNumberAttributeType);
				nhifNumberAttribute.setCreator(Context.getAuthenticatedUser());
				nhifNumberAttribute.setDateCreated(new Date());
				nhifNumberAttribute.setPerson(patient);
				
			}
			nhifNumberAttribute.setValue(nhifNumber);
			patient.addAttribute(nhifNumberAttribute);
		}
		
	}
	
	private void getStudentIdPersonAttribute(Map<String, String> attributes, Patient patient) {
		String studentId = attributes.get("studentId");
		PersonAttributeType studentIdAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(
		    EhrCommonMetadata._EhrPersonAttributeType.STUDENT_ID);
		PersonAttribute studentIdAttribute = patient.getAttribute(studentIdAttributeType);
		if (!studentId.isEmpty()) {
			if (studentIdAttribute == null) {
				studentIdAttribute = new PersonAttribute();
				studentIdAttribute.setAttributeType(studentIdAttributeType);
				studentIdAttribute.setCreator(Context.getAuthenticatedUser());
				studentIdAttribute.setDateCreated(new Date());
				studentIdAttribute.setPerson(patient);
				
			}
			studentIdAttribute.setValue(studentId);
			patient.addAttribute(studentIdAttribute);
		}
		
	}
	
	private void getWaiverNumberPersonAttribute(Map<String, String> attributes, Patient patient) {
		String waiverNumber = attributes.get("waiverNumber");
		PersonAttributeType waiverNumberAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(
		    EhrCommonMetadata._EhrPersonAttributeType.WAIVER_NUMBER);
		PersonAttribute waiverNumberAttribute = patient.getAttribute(waiverNumberAttributeType);
		if (!waiverNumber.isEmpty()) {
			if (waiverNumberAttribute == null) {
				waiverNumberAttribute = new PersonAttribute();
				waiverNumberAttribute.setAttributeType(waiverNumberAttributeType);
				waiverNumberAttribute.setCreator(Context.getAuthenticatedUser());
				waiverNumberAttribute.setDateCreated(new Date());
				waiverNumberAttribute.setPerson(patient);
				
			}
			waiverNumberAttribute.setValue(waiverNumber);
			patient.addAttribute(waiverNumberAttribute);
		}
		
	}
	
	private void consolidateAllPersonalAttributes(Map<String, String> attributes, Patient patient) {
		// call all the methods that saves the person attribute
		getPatientCategoryPersonAttributesPerTheSession(attributes, patient);
		getPayingCategoryPersonAttribute(attributes, patient);
		getFileNumberPersonAttribute(attributes, patient);
		getUniversityPersonAttribute(attributes, patient);
		getNhifNumberPersonAttribute(attributes, patient);
		getStudentIdPersonAttribute(attributes, patient);
		getWaiverNumberPersonAttribute(attributes, patient);
	}
	
	private void sendPatientsToBilling(Concept serviceFee, Encounter encounter) {
		Concept hospitalChargesConcept = Context.getConceptService()
		        .getConceptByUuid("eb458ded-1fa0-4c1b-92fa-322cada4aff2");
		BillableService billableService = Context.getService(BillingService.class).getServiceByConceptId(serviceFee.getId());
		if (billableService != null) {
			OpdTestOrder opdTestOrder = new OpdTestOrder();
			opdTestOrder.setPatient(encounter.getPatient());
			opdTestOrder.setEncounter(encounter);
			opdTestOrder.setConcept(hospitalChargesConcept);
			opdTestOrder.setTypeConcept(DepartmentConcept.TYPES[2]);
			opdTestOrder.setValueCoded(Context.getConceptService().getConcept(serviceFee.getId()));
			opdTestOrder.setCreator(Context.getAuthenticatedUser());
			opdTestOrder.setCreatedOn(new Date());
			opdTestOrder.setBillableService(billableService);
			opdTestOrder.setScheduleDate(new Date());
			opdTestOrder.setFromDept("Registration");
			if (billableService.getPrice() != null && billableService.getPrice().compareTo(BigDecimal.ZERO) == 0) {
				opdTestOrder.setBillingStatus(1);
			}
			HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
			List<PersonAttribute> pas = hcs.getPersonAttributes(encounter.getPatient().getPatientId());
			
			for (PersonAttribute pa : pas) {
				String attributeValue = pa.getValue();
				if (attributeValue.equals("Non paying")) {
					opdTestOrder.setBillingStatus(1);
					break;
				}
			}
			Context.getService(PatientDashboardService.class).saveOrUpdateOpdOrder(opdTestOrder);
		}
	}
	
	private void sendToBillingDependingOnTheBill(Map<String, String> parameters, Encounter encounter, int payCat)
	        throws ParseException {
		Concept registrationFeesConcept = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_REGISTRATION_FEE);
		Concept revisitFeeConcept = Context.getConceptService().getConcept(
		    InitialPatientQueueConstants.CONCEPT_NAME_REVISIT_FEES);
		Concept specialClinicFeeConcept = Context.getConceptService().getConceptByUuid(
		    InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_CLINIC_FEES);
		Concept specialClinicRevisitFeeConcept = Context.getConceptService().getConceptByUuid(
		    InitialPatientQueueConstants.SPECIAL_CLINIC_REVISIT_FEES_UUID);
		//find the special clinic
		int roomToVisit = Integer.parseInt(parameters.get("rooms1"));
		if (payCat == 1) {
			//check if is a revisit or a new patient
			if (hasRevisits(encounter.getPatient())) {
				sendPatientsToBilling(revisitFeeConcept, encounter);
			} else {
				// just save the registration fees
				sendPatientsToBilling(registrationFeesConcept, encounter);
			}
			//check if this patient is going for any special clinic
			if (roomToVisit == 3 && EhrRegistrationUtils.hasSpecialClinicVisit(encounter.getPatient())) {
				sendPatientsToBilling(specialClinicRevisitFeeConcept, encounter);
			} else if (roomToVisit == 3 && !EhrRegistrationUtils.hasSpecialClinicVisit(encounter.getPatient())) {
				sendPatientsToBilling(specialClinicFeeConcept, encounter);
			}
		}
		
	}
	
	private Date getAminuteBefore() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -1);
		return cal.getTime();
	}
	
	private void savePatientSearch(Patient patient) {
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		PatientSearch patientSearch = new PatientSearch();
		String givenName = "";
		String fullname = "";
		String middleName = "";
		String familyName = "";
		Timestamp birtDate = null;
		PatientIdentifier patientIdentifier;
		if (patient != null && hospitalCoreService.getPatientByPatientId(patient.getPatientId()) == null) {
			//log.error("Starting with patient>>" + patient.getPatientId());
			givenName = patient.getGivenName();
			familyName = patient.getFamilyName();
			if (patient.getMiddleName() != null) {
				middleName = patient.getMiddleName();
			}
			fullname = givenName + " " + middleName + " " + familyName;
			birtDate = new Timestamp(patient.getBirthdate().getTime());
			patientIdentifier = patient.getPatientIdentifier();
			
			if (patientIdentifier != null) {
				patientSearch.setPatientId(patient.getPatientId());
				patientSearch.setIdentifier(patientIdentifier.getIdentifier());
				patientSearch.setFullname(fullname);
				patientSearch.setGivenName(givenName);
				patientSearch.setMiddleName(middleName);
				patientSearch.setFamilyName(familyName);
				patientSearch.setGender(patient.getGender());
				patientSearch.setBirthdate(birtDate);
				patientSearch.setAge(patient.getAge());
				patientSearch.setPersonNameId(patient.getPersonName().getPersonNameId());
				patientSearch.setDead(false);
				patientSearch.setAdmitted(false);
				//commit the patient object in the patient_search table
				hospitalCoreService.savePatientSearch(patientSearch);
			}
			
		}
	}
}
