package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.Patient;
import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.initialpatientqueueapp.Utils;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

@AppPage(InitialPatientQueueConstants.APP_PATIENT_QUEUE)
public class MetabaseCategoriesPageController {
	
	public void controller(PageModel model, @RequestParam(value = "patientId", required = false) Patient patient) {
		model.addAttribute("url1", Utils.getDashBoards(103, patient.getPatientId()));
		model.addAttribute("url2", Utils.getDashBoards(104, patient.getPatientId()));
		model.addAttribute("url3", Utils.getDashBoards(99, patient.getPatientId()));
		model.addAttribute("url4", Utils.getDashBoards(103, patient.getPatientId()));
		
	}
}
