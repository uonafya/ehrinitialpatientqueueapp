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
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Visit;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ehrconfigs.metadata.EhrCommonMetadata;
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
	
	public void controller(@FragmentParam("patient") Patient patient, FragmentModel model) {
		
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
			if (nonPayingCategoryMap.containsKey(ca.getAnswerConcept().getConceptId()) == false) {
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
	        @RequestParam("patientId") Patient patient, @RequestParam("paym_1") String paymentCategory) throws IOException {
		
		Map<String, String> parameters = RegistrationWebUtils.optimizeParameters(request);
		
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
		List<Visit> patientVisit = Context.getVisitService().getActiveVisitsByPatient(patient);
		KenyaEmrService kenyaEmrService = Context.getService(KenyaEmrService.class);
		model.addAttribute("userLocation", kenyaEmrService.getDefaultLocation().getName());
		model.addAttribute("receiptDate", new Date());
		consolidateAllPersonalAttributes(parameters, patient);
		try {
			// create encounter for the visit here
			Encounter encounter = createEncounter(patient, parameters);
			encounter = Context.getEncounterService().saveEncounter(encounter);
			//create a visit if not created yet CHECKING IN OF PATIENT
			hasActiveVisit(patientVisit, patient, encounter);
			
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
			//return null;
		}
		return "redirect:"
		        + uiUtils.pageLink("initialpatientqueueapp", "showPatientInfo?patientId=" + patient.getPatientId()
		                + "&visit=" + hasRevisits(patient) + "&payCategory=" + paymentCategory);
	}
	
	/**
	 * Create Encounter For The Visit Of Patient
	 * 
	 * @param patient
	 * @param parameters
	 * @return
	 */
	private Encounter createEncounter(Patient patient, Map<String, String> parameters) {
		
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
					nPayn = "GENERAL";
				} else if (paymt2 == 2) {
					nPayn = "CHILD LESS THAN 5 YEARS";
				} else if (paymt2 == 3) {
					nPayn = "EXPECTANT MOTHER";
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
			
			Concept triageConcept = Context.getConceptService().getConcept(InitialPatientQueueConstants.CONCEPT_NAME_TRIAGE);
			
			Concept selectedTRIAGEConcept = Context.getConceptService().getConcept(tNTriage);
			
			String selectedCategory = paymt3;
			Obs triageObs = new Obs();
			triageObs.setConcept(triageConcept);
			triageObs.setValueCoded(selectedTRIAGEConcept);
			encounter.addObs(triageObs);
			RegistrationWebUtils.sendPatientToTriageQueue(patient, selectedTRIAGEConcept, hasRevisits(patient),
			    selectedCategory);
		} else if (!StringUtils.isBlank(oNOpd)) {
			Concept opdConcept = Context.getConceptService().getConcept(InitialPatientQueueConstants.CONCEPT_NAME_OPD_WARD);
			Concept selectedOPDConcept = Context.getConceptService().getConcept(oNOpd);
			String selectedCategory = paymt3;
			Obs opdObs = new Obs();
			opdObs.setConcept(opdConcept);
			opdObs.setValueCoded(selectedOPDConcept);
			encounter.addObs(opdObs);
			RegistrationWebUtils.sendPatientToOPDQueue(patient, selectedOPDConcept, hasRevisits(patient), selectedCategory);
			
		} else {
			Concept specialClinicConcept = Context.getConceptService().getConcept(
			    InitialPatientQueueConstants.CONCEPT_NAME_SPECIAL_CLINIC);
			//PatientQueueService queueService = (PatientQueueService) Context.getService(PatientQueueService.class);
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
		Concept cnp = Context.getConceptService().getConcept(InitialPatientQueueConstants.CONCEPT_NEW_PATIENT);
		Obs obsn = new Obs();
		obsn.setConcept(cnrf);
		obsn.setValueCoded(cnp);
		double doubleVal = Double.parseDouble(GlobalPropertyUtil.getString(
		    InitialPatientQueueConstants.FORM_FIELD_REGISTRATION_FEE, "0.0"));
		obsn.setValueNumeric(doubleVal);
		obsn.setValueText(paymt3);
		
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
	
	private boolean hasRevisits(Patient patient) {
		boolean found = false;
		List<Visit> visits = Context.getVisitService().getVisitsByPatient(patient);
		System.out.println("### Total visits" + visits.size());
		//check the last visit date if the total visits is greator than 1
		if (visits.size() > 0) {
			Visit visit = visits.get((visits.size()) - 1);
			if (visit.getDateCreated().compareTo(new Date()) < 0) {
				found = true;
			}
		}
		return found;
	}
	
	private void hasActiveVisit(List<Visit> visits, Patient patient, Encounter encounter) {
		VisitService visitService = Context.getVisitService();
		if (visits.size() == 0) {
			Visit visit = new Visit();
			visit.addEncounter(encounter);
			visit.setPatient(patient);
			visit.setVisitType(visitService.getVisitTypeByUuid("3371a4d4-f66f-4454-a86d-92c7b3da990c"));
			visit.setStartDatetime(new Date());
			visit.setLocation(Context.getLocationService().getLocation(1));
			visit.setCreator(Context.getAuthenticatedUser());
			visitService.saveVisit(visit);
		} else {
			//pick the last visit and check if it is still active
			Visit lastVisit = visits.get(visits.size() - 1);
			if (lastVisit.getStartDatetime() != null && lastVisit.getStopDatetime() != null) {
				//this means there is no active visit, we will end up creating one for this patient
				Visit visit1 = new Visit();
				visit1.addEncounter(encounter);
				visit1.setPatient(patient);
				visit1.setVisitType(visitService.getVisitTypeByUuid("3371a4d4-f66f-4454-a86d-92c7b3da990c"));
				visit1.setStartDatetime(new Date());
				visit1.setLocation(Context.getLocationService().getLocation(1));
				visit1.setCreator(Context.getAuthenticatedUser());
				visitService.saveVisit(visit1);
			}
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
}
