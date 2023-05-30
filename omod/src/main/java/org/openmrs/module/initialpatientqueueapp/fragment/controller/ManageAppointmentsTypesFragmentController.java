package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.VisitType;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointmentType;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
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
	
	public String createAppointmentType(@RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "appointmentVisitType") VisitType appointmentVisitType,
	        @RequestParam(value = "appointmentDuration", required = false) String appointmentDuration,
	        @RequestParam(value = "description", required = false) String description) {
		
		EhrAppointmentService ehrAppointmentService = Context.getService(EhrAppointmentService.class);
		EhrAppointmentType ehrAppointmentType = new EhrAppointmentType();
		ehrAppointmentType.setName(name);
		ehrAppointmentType.setCreator(Context.getAuthenticatedUser());
		ehrAppointmentType.setDateCreated(new Date());
		ehrAppointmentType.setVisitType(appointmentVisitType);
		ehrAppointmentType.setDuration(Integer.valueOf(appointmentDuration));
		ehrAppointmentType.setDescription(description);
		//save the appointment type
		ehrAppointmentService.saveEhrAppointmentType(ehrAppointmentType);
		
		return "Appointment type created";
	}
	
	public String editAppointmentType(
	        @RequestParam(value = "appointmentTypeId", required = false) Integer appointmentTypeId,
	        @RequestParam(value = "editName", required = false) String editName,
	        @RequestParam(value = "editAppointmentVisitType") VisitType editAppointmentVisitType,
	        @RequestParam(value = "editAppointmentDuration", required = false) String editAppointmentDuration,
	        @RequestParam(value = "editDescription", required = false) String editDescription,
	        @RequestParam(value = "editAction", required = false) Integer editAction) {
		if (editAction != null) {
			if (editAction == 1) {
				//Call the edit function/saveOrUpdate
			} else if (editAction == 2) {
				//call the saveOrUpdate function with the voided set to 1
			} else if (editAction == 3) {
				//call the purge method and remove the type from the database
			}
		}
		return "Appointment type edited";
	}
	
}
