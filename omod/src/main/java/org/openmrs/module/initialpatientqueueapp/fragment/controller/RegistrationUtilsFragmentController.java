package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.initialpatientqueueapp.includable.validator.attribute.PatientAttributeValidatorService;
import org.openmrs.module.initialpatientqueueapp.model.BirthDateModel;
import org.openmrs.module.initialpatientqueueapp.model.RegistrationRequest;
import org.openmrs.module.initialpatientqueueapp.web.controller.utils.RegistrationWebUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class RegistrationUtilsFragmentController {
	
	private static Log logger = LogFactory.getLog(RegistrationUtilsFragmentController.class);
	
	public SimpleObject processPatientBirthDate(@RequestParam("birthdate") String birthdate, FragmentModel model,
	        UiUtils utils) throws ParseException {
		BirthDateModel dateModel = new BirthDateModel();
		// try to parse date
		// if success -> it's a birthdate
		// otherwise -> it's an age
		Date date = null;
		try {
			date = EhrRegistrationUtils.parseDate(birthdate);
		}
		catch (ParseException e) {
			
		}
		if (date != null) {
			
			if (isLaterToday(date)) {
				dateModel.setError("Birthdate must be before the current date.");
			} else {
				// the user entered the correct birthdate
				dateModel.setEstimated(false);
				dateModel.setBirthdate(birthdate);
				dateModel.setAge(EhrRegistrationUtils.estimateAge(birthdate).replace("~", ""));
				dateModel.setAgeInYear(EhrRegistrationUtils.estimateAgeInYear(birthdate));
				logger.info("User entered the correct birthdate.");
			}
			
		} else {
			
			String lastLetter = birthdate.substring(birthdate.length() - 1).toLowerCase();
			if ("ymwd".indexOf(lastLetter) < 0) {
				dateModel.setError("Age in wrong format");
			} else {
				try {
					dateModel.setEstimated(true);
					String estimatedBirthdate = getEstimatedBirthdate(birthdate);
					dateModel.setBirthdate(estimatedBirthdate);
					dateModel.setAge(EhrRegistrationUtils.estimateAge(estimatedBirthdate));
					dateModel.setAgeInYear(EhrRegistrationUtils.estimateAgeInYear(estimatedBirthdate));
				}
				catch (Exception e) {
					dateModel.setError("Error Processing Date" + e.getMessage());
				}
			}
		}
		//model.addAttribute("json", json);
		return SimpleObject.create("datemodel", dateModel);
	}
	
	public SimpleObject main(FragmentModel model, @RequestParam(value = "nationalId", required = false) String nationalId,
	        @RequestParam(value = "passportNumber", required = false) String passportNumber) {
		//       // RegistrationService registrationService = Context
		//                .getService(RegistrationService.class);
		//        if (nationalId != null) {
		//            Integer nid = registrationService.getNationalId(nationalId);
		////            model.addAttribute("nid", nid);
		//            return SimpleObject.create("nid", nid);
		//
		//        } else {
		//            Integer pnum = registrationService.getPassportNumber(passportNumber);
		////            model.addAttribute("pnum", pnum);
		//            StringWriter out = new StringWriter();
		return SimpleObject.create("pnum", "");
	}
	
	/**
	 * Check whether a day is later than today
	 * 
	 * @param date
	 * @return
	 */
	private boolean isLaterToday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return date.after(c.getTime());
	}
	
	/*
	 * Estimate the birthdate by age
	 *
	 * @param age
	 *
	 * @return
	 */
	private String getEstimatedBirthdate(String text) throws Exception {
		text = text.toLowerCase();
		String ageStr = text.substring(0, text.length() - 1);
		String type = text.substring(text.length() - 1);
		int age = Integer.parseInt(ageStr);
		if (age < 0) {
			throw new Exception("Age must not be negative number!");
		}
		Calendar date = Calendar.getInstance();
		if (type.equalsIgnoreCase("y")) {
			date.add(Calendar.YEAR, -age);
		} else if (type.equalsIgnoreCase("m")) {
			date.add(Calendar.MONTH, -age);
		} else if (type.equalsIgnoreCase("w")) {
			date.add(Calendar.WEEK_OF_YEAR, -age);
		} else if (type.equalsIgnoreCase("d")) {
			date.add(Calendar.DATE, -age);
		}
		return EhrRegistrationUtils.formatDate(date.getTime());
	}
	
	public String post(HttpServletRequest request, UiUtils uiUtils, FragmentModel model) {
		RegistrationRequest request1 = new RegistrationRequest();
		// list all parameter submitted
		Map<String, String> parameters = RegistrationWebUtils.optimizeParameters(request);
		System.out.println(parameters);
		logger.info("Submited parameters: " + parameters);
		
		//        Patient patient;
		//        try {
		//            // create patient
		//            patient = generatePatient(parameters);
		//            System.out.println("before save: "+patient);
		//            patient = Context.getPatientService().savePatient(patient);
		//            RegistrationUtils.savePatientSearch(patient);
		//            System.out.println("After save: "+patient);
		//            logger.info(String.format("Saved new patient [id=%s]",
		//                    patient.getId()));
		//
		//            // create encounter for the visit
		//            Encounter encounter = createEncounter(patient, parameters);
		//            encounter = Context.getEncounterService().saveEncounter(encounter);
		//            logger.info(String
		//                    .format("Saved encounter for the visit of patient [id=%s, patient=%s]",
		//                            encounter.getId(), patient.getId()));
		//
		//            model.addAttribute("status", "success");
		//            request1.setPatientId(patient.getPatientId());
		//            model.addAttribute("patientId", patient.getPatientId());
		//            request1.setStatus("success");
		//            model.addAttribute("encounterId", encounter.getId());
		//            request1.setEncounterId(encounter.getId());
		//        } catch (Exception e) {
		//
		//            e.printStackTrace();
		//            model.addAttribute("status", "error");
		//            request1.setStatus("error");
		//            model.addAttribute("message", e.getMessage());
		//            request1.setMessage(e.getMessage());
		//        }
		//return "/module/registration/patient/savePatient";
		
		//make a dummy return URL for now
		//change this later
		return "redirect:" + uiUtils.pageLink("registration", "statuses");
		//        return SimpleObject.create("json",request1);
		
	}
	
	/**
	 * Generate Patient From Parameters
	 * 
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	private Patient generatePatient(Map<String, String> parameters) throws Exception {
		
		Patient patient = new Patient();
		
		// get person name
		/*if (!StringUtils.isBlank(parameters
		        .get(InitialPatientQueueConstants.FORM_FIELD_PATIENT_FIRSTNAME))
		        && !StringUtils
		        .isBlank(parameters
		                .get(RegistrationConstants.FORM_FIELD_PATIENT_SURNAME))) {
		    PersonName personName = RegistrationUtils
		            .getPersonName(
		                    null,
		                    parameters
		                            .get(RegistrationConstants.FORM_FIELD_PATIENT_FIRSTNAME),
		                    parameters
		                            .get(RegistrationConstants.FORM_FIELD_PATIENT_OTHERNAME),
		                    parameters
		                            .get(RegistrationConstants.FORM_FIELD_PATIENT_SURNAME));
		    patient.addName(personName);
		}*/
		
		// get identifier
		/*if (!StringUtils.isBlank(parameters
		        .get(InitialPatientQueueConstants.FORM_FIELD_PATIENT_IDENTIFIER))) {
		    PatientIdentifier identifier = RegistrationUtils
		            .getPatientIdentifier(parameters
		                    .get(RegistrationConstants.FORM_FIELD_PATIENT_IDENTIFIER));
		    patient.addIdentifier(identifier);
		}*/
		
		// get birthdate
		/*if (!StringUtils.isBlank(parameters
		        .get(RegistrationConstants.FORM_FIELD_PATIENT_BIRTHDATE))) {
		    patient.setBirthdate(RegistrationUtils.parseDate(parameters
		            .get(RegistrationConstants.FORM_FIELD_PATIENT_BIRTHDATE)));
		    if (parameters
		            .get(RegistrationConstants.FORM_FIELD_PATIENT_BIRTHDATE_ESTIMATED)
		            .contains("true")) {
		        patient.setBirthdateEstimated(true);
		    }
		}*/
		
		// get gender
		/*if (!StringUtils.isBlank(parameters
		        .get(RegistrationConstants.FORM_FIELD_PATIENT_GENDER))) {
		    patient.setGender(parameters
		            .get(RegistrationConstants.FORM_FIELD_PATIENT_GENDER));
		}

		// get address
		if (!StringUtils
		        .isBlank(parameters
		                .get(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_DISTRICT))) {
		    patient.addAddress(RegistrationUtils.getPersonAddress(
		            null,
		            parameters
		                    .get(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_POSTALADDRESS),
		            parameters
		                    .get(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_DISTRICT),
		            parameters
		                    .get(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_UPAZILA),
		            parameters
		                    .get(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_LOCATION)));
		}*/
		
		// get custom person attribute
		PatientAttributeValidatorService validator = new PatientAttributeValidatorService();
		Map<String, Object> validationParameters = HospitalCoreUtils.buildParameters("patient", patient, "attributes",
		    parameters);
		String validateResult = validator.validate(validationParameters);
		logger.info("Attirubte validation: " + validateResult);
		if (StringUtils.isBlank(validateResult)) {
			for (String name : parameters.keySet()) {
				if ((name.contains(".attribute.")) && (!StringUtils.isBlank(parameters.get(name)))) {
					String[] parts = name.split("\\.");
					String idText = parts[parts.length - 1];
					Integer id = Integer.parseInt(idText);
					PersonAttribute attribute = EhrRegistrationUtils.getPersonAttribute(id, parameters.get(name));
					patient.addAttribute(attribute);
				}
			}
		} else {
			throw new Exception(validateResult);
		}
		
		return patient;
	}
}
