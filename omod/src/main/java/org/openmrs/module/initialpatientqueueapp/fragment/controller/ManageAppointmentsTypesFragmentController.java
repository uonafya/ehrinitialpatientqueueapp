package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointmentType;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.List;

public class ManageAppointmentsTypesFragmentController {
	
	public void controller(FragmentModel model) {
		VisitService visitService = Context.getVisitService();
		EhrAppointmentService appointmentService = Context.getService(EhrAppointmentService.class);
		List<EhrAppointmentType> appointmentTypeList = new ArrayList<EhrAppointmentType>(
		        appointmentService.getAllEhrAppointmentTypes());
		
		model.addAttribute("types", visitService.getAllVisitTypes());
		model.addAttribute("appointmentTypes", appointmentTypeList);
	}
}
