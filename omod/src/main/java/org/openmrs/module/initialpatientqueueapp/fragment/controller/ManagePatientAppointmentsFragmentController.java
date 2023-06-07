package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.hospitalcore.model.EhrAppointmentSimplifier;
import org.openmrs.module.hospitalcore.model.EhrAppointmentType;
import org.openmrs.module.hospitalcore.model.EhrTimeSlot;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagePatientAppointmentsFragmentController {
	
	public void controller(FragmentModel model) {
		
		EhrAppointmentService ehrAppointmentService = Context.getService(EhrAppointmentService.class);
		List<EhrAppointmentType> appointmentTypeList = new ArrayList<EhrAppointmentType>(
		        ehrAppointmentService.getAllEhrAppointmentTypes());
		
		EhrAppointmentSimplifier ehrAppointmentSimplifier;
		List<EhrAppointmentSimplifier> simplifierList = new ArrayList<EhrAppointmentSimplifier>();
		for (EhrAppointment ehrAppointment : ehrAppointmentService.getScheduledEhrAppointmentsForPatients()) {
			ehrAppointmentSimplifier = new EhrAppointmentSimplifier();
			ehrAppointmentSimplifier.setAppointmentType(ehrAppointment.getAppointmentType().getName());
			ehrAppointmentSimplifier.setPatientId(ehrAppointment.getPatient().getPatientId());
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
		model.addAttribute("allPatientAppointments", simplifierList);
		model.addAttribute("appointmentTypes", appointmentTypeList);
		model.addAttribute("providerList", Context.getProviderService().getAllProviders());
		
	}
	
	public String editAppointments(
			@RequestParam(value = "editAppointmentId", required = false) Integer editAppointmentId,
	        @RequestParam(value = "editAppointmentStatus", required = false) Integer editAppointmentStatus,
	        @RequestParam(value = "editAppointmentVoidReason", required = false) String editAppointmentVoidReason,
	        @RequestParam(value = "editAppointmentAction", required = false) Integer editAppointmentAction, UiUtils uiUtils) {
		
		EhrAppointmentService ehrAppointmentService = Context.getService(EhrAppointmentService.class);
		EhrAppointment ehrAppointment = ehrAppointmentService.getEhrAppointment(editAppointmentId);
		Map<String, Object> params = new HashMap<>();
		params.put("patientId", ehrAppointment.getPatient().getPatientId());

		if (editAppointmentAction != null) {
			if (editAppointmentAction == 1) {
				//redirect to the patient queue
				return "redirect:" + uiUtils.pageLink("initialpatientqueueapp", "patientCategory", params);
			} else if (editAppointmentAction == 2 && editAppointmentStatus != null) {
				//redirect to edit appointment
				ehrAppointment.setStatus(status(editAppointmentStatus));
				ehrAppointmentService.saveEhrAppointment(ehrAppointment);
			} else if (editAppointmentAction == 3 && StringUtils.isNotBlank(editAppointmentVoidReason)) {
				//redirect to void an appointment
				ehrAppointment.setVoidReason(editAppointmentVoidReason);
				ehrAppointment.setVoided(true);
				ehrAppointment.setVoidedBy(Context.getAuthenticatedUser());
				ehrAppointmentService.saveEhrAppointment(ehrAppointment);
			} else if (editAppointmentAction == 4) {
				//redirect to delete an appointment
				ehrAppointmentService.purgeEhrAppointment(ehrAppointment);
			}
		}
		return "Appointment list updated";
	}
	
	private EhrAppointment.EhrAppointmentStatus status(Integer status) {
		if (status == 1 || status == 2) {
			return EhrAppointment.EhrAppointmentStatus.SCHEDULED;
		} else if (status == 3) {
			return EhrAppointment.EhrAppointmentStatus.CANCELLED;
		} else if (status == 4) {
			return EhrAppointment.EhrAppointmentStatus.MISSED;
		} else if (status == 5) {
			return EhrAppointment.EhrAppointmentStatus.COMPLETED;
		}
		return null;
	}
}
