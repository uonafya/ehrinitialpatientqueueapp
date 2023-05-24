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
	}
	
}
