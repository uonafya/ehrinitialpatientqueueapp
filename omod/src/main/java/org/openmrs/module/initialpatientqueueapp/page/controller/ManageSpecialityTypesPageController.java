package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.service.SpecialityService;
import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.page.PageModel;

@AppPage(InitialPatientQueueConstants.APP_EHR_APPOINTMENTS)
public class ManageSpecialityTypesPageController {
	
	public void controller(PageModel model) {
		SpecialityService specialityService = Context.getService(SpecialityService.class);
		model.addAttribute("specialityTypes", specialityService.getAllSpecialities());
	}
}
