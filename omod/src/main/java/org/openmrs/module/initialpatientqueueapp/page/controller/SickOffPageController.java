package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;
import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.hospitalcore.model.SickOff;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.User;
import org.openmrs.Patient;
import org.openmrs.ui.framework.UiUtils;

import java.util.Date;

@AppPage(InitialPatientQueueConstants.APP_PATIENT_QUEUE)
public class SickOffPageController {
	
	public void controller(PageModel pageModel, @RequestParam("patientId") Patient patient) {
		
		pageModel.addAttribute("patient", patient);
		//today
		//today-3months
		//		pageModel.addAttribute("clientSickoffs",
		//		    Context.getService(HospitalCoreService.class).getPatientSickOffs(patient, new Date(), new Date()));
	}
	
	public String post(UiUtils uiUtils, @RequestParam("patientId") Patient patient, @RequestParam("user") User user,
	        @RequestParam(value = "sickOffStartDate", required = false) Date sickOffStartDate,
	        @RequestParam(value = "clinicianNotes", required = false) String clinicianNotes) {
		//get sick-off form data and save to db
		//provider,dateOfOnset,clinicalNotes,patient.patientId
		SickOff sickOff = new SickOff();
		sickOff.setCreator(Context.getAuthenticatedUser());
		sickOff.setClinicianNotes(clinicianNotes);
		sickOff.setSickOffStartDate(sickOffStartDate);
		System.out.println(sickOff.toString());
		
		try {
			Context.getService(HospitalCoreService.class).savePatientSickOff(sickOff);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + uiUtils.pageLink("initialpatientqueueapp", "sickOff?patientId=" + patient.getPatientId());
		
	}
}
