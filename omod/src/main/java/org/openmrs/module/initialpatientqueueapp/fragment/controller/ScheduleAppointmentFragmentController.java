package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.model.AppointmentKind;
import org.openmrs.module.appointments.model.AppointmentProvider;
import org.openmrs.module.appointments.model.AppointmentProviderResponse;
import org.openmrs.module.appointments.model.AppointmentStatus;
import org.openmrs.module.appointments.service.AppointmentServiceDefinitionService;
import org.openmrs.module.appointments.service.AppointmentsService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.EhrAppointmentSimplifier;
import org.openmrs.module.hospitalcore.model.SickOff;
import org.openmrs.module.hospitalcore.util.DateUtils;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScheduleAppointmentFragmentController {
	
	public void controller(FragmentModel model, @FragmentParam(value = "patientId", required = false) Patient patient) {
		ProviderService providerService = Context.getProviderService();
		AppointmentServiceDefinitionService appointmentServiceDefinitionService = Context
		        .getService(AppointmentServiceDefinitionService.class);
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		
		model.addAttribute("appointmentServices", appointmentServiceDefinitionService.getAllAppointmentServices(false));
		model.addAttribute("providerList", providerService.getAllProviders());
		model.addAttribute("patientId", patient.getPatientId());
		model.addAttribute("appointmentServicesTypes", hospitalCoreService.getAppointmentServiceType());
		
		EhrAppointmentSimplifier ehrAppointmentSimplifier;
		List<EhrAppointmentSimplifier> simplifierList = new ArrayList<EhrAppointmentSimplifier>();
		for (Appointment ehrAppointment : hospitalCoreService.getPatientAppointments(patient)) {
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
			ehrAppointmentSimplifier.setStatus(ehrAppointment.getStatus().name());
			
			simplifierList.add(ehrAppointmentSimplifier);
		}
		model.addAttribute("patientAppointments", simplifierList);
	}
	
	public String createAppointment(@RequestParam(value = "appointmentNumber", required = false) String appointmentNumber,
	        @RequestParam(value = "patientId", required = false) Patient patient,
	        @RequestParam(value = "service") String service,
	        @RequestParam(value = "serviceType", required = false) String serviceType,
	        @RequestParam(value = "provider") Provider provider,
	        @RequestParam(value = "startDateTime", required = false) Date startDateTime,
	        @RequestParam(value = "endDateTime", required = false) Date endDateTime,
	        @RequestParam(value = "comments", required = false) String comments) throws ParseException {
		
		Location location = Context.getService(KenyaEmrService.class).getDefaultLocation();
		AppointmentsService appointmentsService = Context.getService(AppointmentsService.class);
		AppointmentServiceDefinitionService appointmentServiceDefinitionService = Context
		        .getService(AppointmentServiceDefinitionService.class);
		Appointment appointment;
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
			
			//AppointmentProvider
			AppointmentProvider appointmentProvider = new AppointmentProvider();
			appointmentProvider.setAppointment(appointment);
			appointmentProvider.setProvider(provider);
			appointmentProvider.setCreator(Context.getAuthenticatedUser());
			appointmentProvider.setDateCreated(new Date());
			appointmentProvider.setResponse(AppointmentProviderResponse.AWAITING);
			Set<AppointmentProvider> appointmentProviderSet = new HashSet<AppointmentProvider>();
			appointmentProviderSet.add(appointmentProvider);
			appointment.setProviders(appointmentProviderSet);
			
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
