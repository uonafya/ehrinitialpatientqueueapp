package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.service.AppointmentsService;
import org.openmrs.module.ehrconfigs.utils.EhrConfigsUtils;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.hospitalcore.model.EhrAppointmentSimplifier;
import org.openmrs.module.hospitalcore.util.DateUtils;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.hospitalcore.util.PatientUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagePatientAppointmentsFragmentController {
	
	public void controller(FragmentModel model) {
		
	}
	
	public List<SimpleObject> fetchAppointmentSummariesByDateRange(
	        @RequestParam(value = "fromDate", required = false) Date startDate,
	        @RequestParam(value = "toDate", required = false) Date endDate, UiUtils ui) {
		
		AppointmentsService appointmentsService = Context.getService(AppointmentsService.class);
		if (startDate == null) {
			startDate = DateUtils.getStartOfDay(new Date());
		} else {
			startDate = DateUtils.getStartOfDay(startDate);
		}
		
		if (endDate == null) {
			endDate = DateUtils.getEndOfDay(new Date());
		} else {
			endDate = DateUtils.getEndOfDay(endDate);
		}
		EhrAppointmentSimplifier ehrAppointmentSimplifier;
		List<EhrAppointmentSimplifier> simplifierList = new ArrayList<EhrAppointmentSimplifier>();
		for (Appointment ehrAppointment : appointmentsService.getAllAppointmentsInDateRange(startDate, endDate)) {
			ehrAppointmentSimplifier = new EhrAppointmentSimplifier();
			ehrAppointmentSimplifier.setAppointmentNumber(ehrAppointment.getAppointmentNumber());
			ehrAppointmentSimplifier.setAppointmentService(ehrAppointment.getService().getName());
			ehrAppointmentSimplifier.setAppointmentServiceType(ehrAppointment.getServiceType().getName());
			ehrAppointmentSimplifier.setProvider(HospitalCoreUtils.getProviderNames(ehrAppointment.getProviders()));
			ehrAppointmentSimplifier.setResponse(HospitalCoreUtils.getProviderResponse(ehrAppointment.getProviders()));
			ehrAppointmentSimplifier.setStartTime(DateUtils.getDateFromDateAsString(ehrAppointment.getStartDateTime(),
			    "yyyy-MM-dd HH:mm"));
			ehrAppointmentSimplifier.setEndTime(DateUtils.getDateFromDateAsString(ehrAppointment.getEndDateTime(),
			    "yyyy-MM-dd HH:mm"));
			ehrAppointmentSimplifier.setAppointmentReason(ehrAppointment.getComments());
			ehrAppointmentSimplifier.setPatientNames(PatientUtils.getFullName(ehrAppointment.getPatient()));
			ehrAppointmentSimplifier.setPatientIdentifier(EhrConfigsUtils.getPreferredPatientIdentifier(ehrAppointment
			        .getPatient()));
			ehrAppointmentSimplifier.setStatus(ehrAppointment.getStatus().name());
			
			simplifierList.add(ehrAppointmentSimplifier);
		}
		
		return SimpleObject.fromCollection(simplifierList, ui, "appointmentNumber", "appointmentService",
		    "appointmentServiceType", "response", "startTime", "endTime", "appointmentReason", "provider", "patientNames",
		    "patientIdentifier", "status");
		
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
