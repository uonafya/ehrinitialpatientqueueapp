package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.model.AppointmentKind;
import org.openmrs.module.appointments.model.AppointmentStatus;
import org.openmrs.module.appointments.service.AppointmentServiceDefinitionService;
import org.openmrs.module.appointments.service.AppointmentsService;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.hospitalcore.model.EhrAppointmentSimplifier;
import org.openmrs.module.hospitalcore.model.EhrAppointmentType;
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
	
	public String createAppointment(@RequestParam(value = "appointmentNumber") String appointmentNumber,
	        @RequestParam(value = "patientId") Patient patient, @RequestParam(value = "service") String service,
	        @RequestParam(value = "serviceType") String serviceType, @RequestParam(value = "provider") Provider provider,
	        @RequestParam(value = "startDateTime") Date startDateTime,
	        @RequestParam(value = "endDateTime") Date endDateTime, @RequestParam(value = "comments") String comments)
	        throws ParseException {
		
		Location location = Context.getService(KenyaEmrService.class).getDefaultLocation();
		AppointmentsService appointmentsService = Context.getService(AppointmentsService.class);
		AppointmentServiceDefinitionService appointmentServiceDefinitionService = Context
		        .getService(AppointmentServiceDefinitionService.class);
		Appointment appointment = null;
		if (StringUtils.isNotBlank(service) && StringUtils.isNotBlank(serviceType) && provider != null
		        && endDateTime != null && startDateTime != null && StringUtils.isNotBlank(appointmentNumber)) {
			
			appointment = new Appointment();
			appointment.setAppointmentNumber(appointmentNumber);
			appointment.setService(appointmentServiceDefinitionService.getAppointmentServiceByUuid(service));
			appointment.setServiceType(appointmentServiceDefinitionService.getAppointmentServiceTypeByUuid(serviceType));
			appointment.setPatient(patient);
			appointment.setProvider(provider);
			appointment.setStartDateTime(startDateTime);
			appointment.setEndDateTime(endDateTime);
			appointment.setComments(comments);
			appointment.setDateCreated(new Date());
			appointment.setCreator(Context.getAuthenticatedUser());
			appointment.setLocation(location);
			appointment.setStatus(AppointmentStatus.Requested);
			appointment.setAppointmentKind(AppointmentKind.Scheduled);
			
			//save the appointment to the DB
			appointmentsService.validateAndSave(appointment);
			
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
