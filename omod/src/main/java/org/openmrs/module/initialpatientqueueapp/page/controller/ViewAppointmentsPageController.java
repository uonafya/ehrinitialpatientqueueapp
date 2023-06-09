package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.hospitalcore.model.EhrAppointmentSimplifier;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		EhrAppointmentService appointmentService = Context.getService(EhrAppointmentService.class);
		List<EhrAppointment> getTodaysAppointments = new ArrayList<EhrAppointment>(
		        appointmentService.getEhrAppointmentsByConstraints(DateUtil.getStartOfDay(scheduledDate),
		            DateUtil.getEndOfDay(scheduledDate), null, null, null, null));
		
		EhrAppointmentSimplifier ehrAppointmentSimplifier;
		List<EhrAppointmentSimplifier> simplifierList = new ArrayList<EhrAppointmentSimplifier>();
		for (EhrAppointment ehrAppointment : getTodaysAppointments) {
			ehrAppointmentSimplifier = new EhrAppointmentSimplifier();
			ehrAppointmentSimplifier.setAppointmentType(ehrAppointment.getAppointmentType().getName());
			ehrAppointmentSimplifier.setPatient(ehrAppointment.getPatient());
			ehrAppointmentSimplifier.setAppointmentReason(ehrAppointment.getReason());
			ehrAppointmentSimplifier.setProvider(ehrAppointment.getTimeSlot().getAppointmentBlock().getProvider().getName());
			ehrAppointmentSimplifier.setStartTime(EhrRegistrationUtils.formatDateTime(ehrAppointment.getTimeSlot()
			        .getStartDate()));
			ehrAppointmentSimplifier.setEndTime(EhrRegistrationUtils.formatDateTime(ehrAppointment.getTimeSlot()
			        .getEndDate()));
			ehrAppointmentSimplifier.setStatus(ehrAppointment.getStatus().getName());
			ehrAppointmentSimplifier.setAppointmentId(ehrAppointment.getAppointmentId());
			simplifierList.add(ehrAppointmentSimplifier);
		}
		model.addAttribute("getTodaysAppointments", simplifierList);
	}
}
