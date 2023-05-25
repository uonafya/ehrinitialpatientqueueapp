package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.LocationService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.hospitalcore.model.EhrAppointmentSimplifier;
import org.openmrs.module.hospitalcore.model.EhrAppointmentType;
import org.openmrs.module.hospitalcore.model.EhrTimeSlot;
import org.openmrs.module.hospitalcore.model.SickOff;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleAppointmentFragmentController {
	
	public void controller(FragmentModel model, @FragmentParam(value = "patientId", required = false) Patient patient) {
		EhrAppointmentService ehrAppointmentService = Context.getService(EhrAppointmentService.class);
		ProviderService providerService = Context.getProviderService();
		List<EhrAppointmentType> appointmentTypeList = new ArrayList<>(ehrAppointmentService.getAllEhrAppointmentTypes());
		List<EhrAppointmentSimplifier> ehrAppointmentSimplifierList = new ArrayList<EhrAppointmentSimplifier>();

		model.addAttribute("appointmentTypes", appointmentTypeList);
		model.addAttribute("providerList", providerService.getAllProviders());
		model.addAttribute("patientAppointments", ehrAppointmentService.getEhrAppointmentsOfPatient(patient));
		model.addAttribute("patientId", patient.getPatientId());
		EhrAppointmentSimplifier ehrAppointmentSimplifier;
		for(EhrAppointment ehrAppointment : ehrAppointmentService.getEhrAppointmentsOfPatient(patient)) {
			ehrAppointmentSimplifier = new EhrAppointmentSimplifier();
			ehrAppointmentSimplifier.setAppointmentType(ehrAppointment.getAppointmentType().getName());
			ehrAppointmentSimplifier.setProvider(ehrAppointment.getTimeSlot().getAppointmentBlock().getProvider().getName());
			ehrAppointmentSimplifier.setStatus(ehrAppointment.getStatus().getName());
			ehrAppointmentSimplifier.setStartTime(EhrRegistrationUtils.formatDateTime(ehrAppointment.getTimeSlot().getStartDate()));
			ehrAppointmentSimplifier.setAppointmentReason(ehrAppointment.getReason());
			ehrAppointmentSimplifier.setEndTime(EhrRegistrationUtils.formatDateTime(ehrAppointment.getTimeSlot().getEndDate()));
			ehrAppointmentSimplifierList.add(ehrAppointmentSimplifier);

		}
		model.addAttribute("patientAppointments", ehrAppointmentSimplifierList);
		model.addAttribute("patientId", patient.getPatientId());
	}
	
	public String createAppointment(@RequestParam(value = "appointmentDate") String appointmentDate,
	        @RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime,
	        @RequestParam(value = "type") Integer type, @RequestParam(value = "patientId") Patient patient,
	        @RequestParam(value = "provider") Provider provider,
	        @RequestParam(value = "notes", required = false) String notes) throws ParseException {
		
		Integer locationId = Context.getService(KenyaEmrService.class).getDefaultLocation().getLocationId();
		EhrAppointmentService appointmentService = Context.getService(EhrAppointmentService.class);
		LocationService locationService = Context.getLocationService();
		EhrAppointmentType ehrAppointmentType = appointmentService.getEhrAppointmentType(type);
		EhrAppointment appointment = new EhrAppointment();
		if (StringUtils.isNotBlank(appointmentDate) && StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			String[] appointmentDatePart = appointmentDate.split("/");
			String year = appointmentDatePart[2];
			String month = appointmentDatePart[1];
			String day = appointmentDatePart[0];
			String resultDateString = year + "-" + month + "-" + day;
			
			String startDateStr = resultDateString + " " + startTime;
			String endDateStr = resultDateString + " " + endTime;
			Date startDate = EhrRegistrationUtils.formatDateFromStringWithTime(startDateStr);
			Date endDate = EhrRegistrationUtils.formatDateFromStringWithTime(endDateStr);
			
			EhrTimeSlot appointmentTimeSlot = EhrRegistrationUtils.getAppointmentTimeSlot(
			    startDate,
			    endDate,
			    provider,
			    locationId == null ? Context.getService(KenyaEmrService.class).getDefaultLocation() : locationService
			            .getLocation(locationId), ehrAppointmentType);
			
			appointmentService.saveEhrAppointmentBlock(appointmentTimeSlot.getAppointmentBlock());
			appointmentService.saveEhrTimeSlot(appointmentTimeSlot);
			appointment.setTimeSlot(appointmentTimeSlot);
			appointment.setPatient(patient);
			appointment.setAppointmentType(ehrAppointmentType);
			appointment.setStatus(EhrAppointment.EhrAppointmentStatus.SCHEDULED);
			if (StringUtils.isNotBlank(notes)) {
				appointment.setReason(notes);
			}
			appointmentService.saveEhrAppointment(appointment);
		}
		return "Appointment Created";
	}
	
	public String saveSickOff(UiUtils uiUtils, @RequestParam("patientId") Patient patientId,
	        @RequestParam("provider") Provider provider,
	        @RequestParam(value = "sickOffStartDate", required = false) Date sickOffStartDate,
	        @RequestParam(value = "sickOffEndDate", required = false) Date sickOffEndDate,
	        @RequestParam(value = "clinicianNotes", required = false) String clinicianNotes) {
		
		SickOff sickOff;
		if (provider != null && sickOffStartDate != null && sickOffEndDate != null) {
			sickOff = new SickOff();
			sickOff.setCreator(Context.getAuthenticatedUser());
			sickOff.setClinicianNotes(clinicianNotes);
			sickOff.setSickOffStartDate(sickOffStartDate);
			sickOff.setCreatedOn(new Date());
			sickOff.setSickOffEndDate(sickOffEndDate);
			sickOff.setProvider(provider);
			sickOff.setPatient(patientId);
			
			try {
				Context.getService(HospitalCoreService.class).savePatientSickOff(sickOff);
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Patient sick off created";
	}
}
