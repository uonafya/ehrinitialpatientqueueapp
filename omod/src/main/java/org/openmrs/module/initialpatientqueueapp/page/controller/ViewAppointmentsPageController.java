package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@AppPage(InitialPatientQueueConstants.APP_PATIENT_QUEUE)
public class ViewAppointmentsPageController

{
	
	public void controller(PageModel model, @RequestParam(required = false, value = "scheduledDate") Date scheduledDate) {
		// Get the date for schedule view
		if (scheduledDate == null) {
			scheduledDate = new Date();
		}
		scheduledDate = DateUtil.getStartOfDay(scheduledDate);
		model.addAttribute("scheduledDate", scheduledDate);
	}
}
