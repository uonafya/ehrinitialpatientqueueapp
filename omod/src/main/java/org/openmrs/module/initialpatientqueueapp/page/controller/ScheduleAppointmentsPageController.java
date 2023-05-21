package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.Patient;
import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

@AppPage(InitialPatientQueueConstants.APP_PATIENT_QUEUE)
public class ScheduleAppointmentsPageController {
	
	public void controller(PageModel model, @RequestParam("patientId") Patient patient) {
		model.addAttribute("patient", patient);
	}
}
