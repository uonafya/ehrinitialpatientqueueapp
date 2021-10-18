/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.initialpatientqueueapp;

public class InitialPatientQueueConstants {
	
	/**
	 * Module ID
	 */
	public static final String MODULE_ID = "initialpatientqueueapp";
	
	public static final String MODULE_ID_EHR_CONFIGS = "ehrconfigs";
	
	public static final String APP_PATIENT_QUEUE = MODULE_ID + ".queue";
	
	public static final String APP_PATIENT_OPD = MODULE_ID + ".opd";
	
	public static final String CONCEPT_NAME_TRIAGE = "Triage Room";
	
	public static final String CONCEPT_NAME_OPD_WARD = "OPD Room";
	
	public static final String CONCEPT_NAME_SPECIAL_CLINIC = "Special Clinic";
	
	public static final String PROPERTY_ENCOUNTER_TYPE_REGINIT = MODULE_ID_EHR_CONFIGS + ".encounterType.init";
	
	public static final String PROPERTY_ENCOUNTER_TYPE_REVISIT = MODULE_ID_EHR_CONFIGS + ".encounterType.revisit";
	
	public static final String PROPERTY_LOCATION = MODULE_ID_EHR_CONFIGS + ".location";
	
	public static final String CONCEPT_NAME_PAYING_CATEGORY = "Paying";
	
	public static final String CONCEPT_NAME_SPECIAL_SCHEME = "Special Scheme";
	
	public static final String CONCEPT_NAME_NONPAYING_CATEGORY = "Non Paying";
	
	public static final String CONCEPT_NAME_LIST_OF_UNIVERSITIES = "Education institution";
	
	public static final String PROPERTY_INITIAL_REGISTRATION_FEE = MODULE_ID_EHR_CONFIGS + ".initialVisitRegistrationFee";
	
	public static final String PROPERTY_CHILDLESSTHANFIVEYEAR_REGISTRATION_FEE = MODULE_ID
	        + ".childLessThanFiveYearRegistrationFee";
	
	public static final String PROPERTY_SPECIALCLINIC_REGISTRATION_FEE = MODULE_ID + ".specialClinicRegistrationFee";
	
	public static final String FORM_FIELD_PATIENT_TRIAGE = "patient.triage";
	
	public static final String FORM_FIELD_PATIENT_OPD_WARD = "patient.opdWard";
	
	public static final String FORM_FIELD_PATIENT_SPECIAL_CLINIC = "patient.specialClinic";
	
	public static final String FORM_FIELD_PAYMENT_CATEGORY = "person.attribute.14";
	
	public static final String CONCEPT_NEW_PATIENT = "New client";
	
	public static final String PROPERTY_IDENTIFIER_PREFIX = MODULE_ID + ".identifier_prefix";
	
	public static final String PROPERTY_PATIENT_IDENTIFIER_TYPE = MODULE_ID_EHR_CONFIGS + ".patientIdentifierType";
	
	public static final String FORM_FIELD_REGISTRATION_FEE = "ehrconfigs.initialVisitRegistrationFee";
	
	public static final String FORM_FIELD_PAYING_CATEGORY = "person.attribute.44";
	
	public static final String FORM_FIELD_NONPAYING_CATEGORY = "person.attribute.45";
	
	public static final String FORM_FIELD_PATIENT_SPECIAL_SCHEME = "person.attribute.46";
	
	public static final String CONCEPT_NAME_PATIENT_REFERRED_FROM = "PATIENT REFERRED FROM";
	
	public static final String CONCEPT_NAME_REGISTRATION_FEE = "Registration Fee";
	
	public static final String CONCEPT_NAME_REVISIT_FEES = "Revisit Fee";
	
	public static final String CONCEPT_NAME_SPECIAL_CLINIC_FEES = "430fc46d-94bb-4fbc-b7bd-894b7cc98058";
	
	public static final String SPECIAL_CLINIC_REVISIT_FEES_UUID = "7da33864-3183-4764-a1e8-d86bca84c9ef";
	
	public static final String CONCEPT_NAME_PATIENT_REFERRED_TO_HOSPITAL = "PATIENT REFERRED TO HOSPITAL?";
	
	public static final String PROPERTY_NUMBER_OF_DATE_VALIDATION = MODULE_ID_EHR_CONFIGS + ".numberOfDateValidation";
	
	public static final String PROPERTY_REVISIT_REGISTRATION_FEE = MODULE_ID_EHR_CONFIGS + ".reVisitRegistrationFee";
	
	public static final String FORM_FIELD_PATIENT_MLC = "patient.mlc";
	
	public static final String CONCEPT_REVISIT = "Revisit Patient";
	
	public static final String FORM_FIELD_SELECTED_PAYMENT_CATEGORY = "patient.selectedPaymentCategory";
	
	public static final String FORM_FIELD_SELECTED_PAYMENT_SUBCATEGORY = "patient.selectedPaymentSubCategory";
	
	public static final String REASONS_FOR_REFERRAL = "1272AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public static final String FORM_FIELD_PATIENT_REFERRED_FROM = "patient.referred.from"; //type of facility referred from
	
	public static final String FORM_FIELD_COUNTY_REFERRED_FROM = "patient.referred.county";
	
	public static final String FACILITY_TYPE_REFERRED_FROM = "160481AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public static final String CONCEPT_MEDICO_LEGAL_CASE = "17b33cd3-1af9-4a1b-a65b-b5e30540b189";
	
	public static final String FORM_FIELD_PATIENT_REFERRED_REASON = "patient.referred.reason";
	
	public static final String FORM_FIELD_PATIENT_REFERRED_DESCRIPTION = "patient.referred.description";
}
