package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.Patient;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.SickOff;
import org.openmrs.module.hospitalcore.model.SickOffSimplifier;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@AppPage(InitialPatientQueueConstants.APP_PATIENT_QUEUE)
public class SickOffPageController {
	
	public void controller(PageModel pageModel, @RequestParam("patientId") Patient patient) {
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		ProviderService providerService = Context.getProviderService();
		
		pageModel.addAttribute("patient", patient);
		SickOffSimplifier sickOffSimplifier;
		List<SickOffSimplifier> patientSearchList = new ArrayList<SickOffSimplifier>();
		for (SickOff sickOff : hospitalCoreService.getPatientSickOffs(patient, null, null)) {
			sickOffSimplifier = new SickOffSimplifier();
			sickOffSimplifier.setSickOffId(sickOff.getSickOffId());
			sickOffSimplifier.setNotes(sickOff.getClinicianNotes());
			sickOffSimplifier.setUser(sickOff.getCreator().getGivenName() + " " + sickOff.getCreator().getFamilyName());
			sickOffSimplifier.setProvider(sickOff.getProvider().getName());
			sickOffSimplifier.setPatientName(sickOff.getPatient().getGivenName() + " "
			        + sickOff.getPatient().getFamilyName());
			sickOffSimplifier.setPatientIdentifier(sickOff.getPatient().getActiveIdentifiers().get(0).getIdentifier());
			sickOffSimplifier.setSickOffStartDate(EhrRegistrationUtils.formatDateAs(sickOff.getSickOffStartDate()));
			sickOffSimplifier.setSickOffEndDate(EhrRegistrationUtils.formatDateAs(sickOff.getSickOffEndDate()));
			sickOffSimplifier.setCreatedOn(EhrRegistrationUtils.formatDateTime(sickOff.getCreatedOn()));
			
			patientSearchList.add(sickOffSimplifier);
		}
		pageModel.addAttribute("sickOffs", patientSearchList);
		pageModel.addAttribute("providerList", providerService.getAllProviders());
		pageModel.addAttribute("patientId", patient.getPatientId());
	}
	
}
