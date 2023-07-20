package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.SickOff;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@AppPage(InitialPatientQueueConstants.APP_PATIENT_QUEUE)
public class SickOffDetailsForPatientPageController {
	
	public void controller(PageModel pageModel, @RequestParam(value = "sickOffId", required = false) Integer sickOff) {
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		SickOff sickOffObj = hospitalCoreService.getPatientSickOffById(sickOff);
		pageModel.addAttribute("currentPatient", sickOffObj.getPatient());
		pageModel.addAttribute("sickOffStartDate", EhrRegistrationUtils.formatDate(sickOffObj.getSickOffStartDate()));
		pageModel.addAttribute("sickOffEndDate", EhrRegistrationUtils.formatDate(sickOffObj.getSickOffEndDate()));
		pageModel.addAttribute("notes", sickOffObj.getClinicianNotes());
		pageModel.addAttribute("sickOffCreatedDate", EhrRegistrationUtils.formatDateTime(sickOffObj.getCreatedOn()));
		pageModel.addAttribute("sickOffCreator", sickOffObj.getCreator().getGivenName() + " "
		        + sickOffObj.getCreator().getFamilyName());
		pageModel.addAttribute("sickOffProvider", sickOffObj.getProvider().getPerson().getGivenName() + " "
		        + sickOffObj.getProvider().getPerson().getFamilyName());
		pageModel.addAttribute("timestamp", EhrRegistrationUtils.formatDateTime(new Date()));
		pageModel
		        .addAttribute("numberOfDays", EhrRegistrationUtils.unitsSinceInteger(sickOffObj.getSickOffEndDate(),
		            sickOffObj.getSickOffStartDate(), "days"));
		
		Patient patient = sickOffObj.getPatient();
		String middleName = "";
		String givenName = "";
		String familyName = "";
		
		if (patient != null) {
			if (patient.getFamilyName() != null) {
				familyName = patient.getFamilyName();
			}
			if (patient.getGivenName() != null) {
				givenName = patient.getGivenName();
			}
			if (patient.getMiddleName() != null) {
				middleName = patient.getMiddleName();
			}
		}
		
		pageModel.addAttribute("names", givenName + " " + familyName + " " + middleName);
	}
}
