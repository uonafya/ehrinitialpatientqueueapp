package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.hospitalcore.model.EhrAppointmentSimplifier;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.List;

public class ManagePatientAppointmentsFragmentController {
	
	public void controller(FragmentModel model) {

		EhrAppointmentService ehrAppointmentService = Context.getService(EhrAppointmentService.class);
		List<EhrAppointment> appointment = new ArrayList<>(ehrAppointmentService.getScheduledEhrAppointmentsForPatients());


		EhrAppointmentSimplifier ehrAppointmentSimplifier;
		List<EhrAppointmentSimplifier> simplifierList = new ArrayList<EhrAppointmentSimplifier>();
		for(EhrAppointment ehrAppointment: ehrAppointmentService.getScheduledEhrAppointmentsForPatients()) {
			ehrAppointmentSimplifier = new EhrAppointmentSimplifier();
			ehrAppointmentSimplifier.setAppointmentType(ehrAppointment.getAppointmentType().getName());
			ehrAppointmentSimplifier.setPatientId(ehrAppointment.getPatient().getPatientId());
			ehrAppointmentSimplifier.setAppointmentReason(ehrAppointment.getReason());
			ehrAppointmentSimplifier.setProvider(ehrAppointment.getTimeSlot().getAppointmentBlock().getProvider().getName());
			ehrAppointmentSimplifier.setStartTime(EhrRegistrationUtils.formatDateTime(ehrAppointment.getTimeSlot().getStartDate()));
			ehrAppointmentSimplifier.setEndTime(EhrRegistrationUtils.formatDateTime(ehrAppointment.getTimeSlot().getEndDate()));
			ehrAppointmentSimplifier.setStatus(ehrAppointment.getStatus().getName());
		}
		model.addAttribute("allPatientAppointments", simplifierList);

	}
}
