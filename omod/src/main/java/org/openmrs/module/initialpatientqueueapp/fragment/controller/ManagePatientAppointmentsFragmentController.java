package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.hospitalcore.util.DateUtils;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManagePatientAppointmentsFragmentController {
	
	public void controller(FragmentModel model) {

		EhrAppointmentService ehrAppointmentService = Context.getService(EhrAppointmentService.class);
		Date startDate = DateUtils.getStartOfDay(new Date());
		Date endDate = DateUtils.getEndOfDay(new Date());
		List<EhrAppointment> appointment = new ArrayList<>(ehrAppointmentService.getEhrAppointmentsByConstraints(startDate, endDate, Context.getService(KenyaEmrService.class).getDefaultLocation()
		, null, null, null,EhrAppointment.EhrAppointmentStatus.SCHEDULED));
		model.addAttribute("allPatientAppointments", appointment);

	}
}
