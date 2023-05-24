package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.List;

public class ManagePatientAppointmentsFragmentController {
	
	public void controller(FragmentModel model) {

		EhrAppointmentService ehrAppointmentService = Context.getService(EhrAppointmentService.class);
		List<EhrAppointment> appointment = new ArrayList<>(ehrAppointmentService.getEhrAppointmentsByConstraints(null, null, Context.getService(KenyaEmrService.class).getDefaultLocation()
		, null, null, EhrAppointment.EhrAppointmentStatus.SCHEDULED));
		System.out.println("The size of the array is >>"+appointment);
		model.addAttribute("allPatientAppointments", appointment);

	}
}
