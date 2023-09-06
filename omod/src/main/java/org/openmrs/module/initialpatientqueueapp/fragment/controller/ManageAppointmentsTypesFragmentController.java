package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.model.AppointmentServiceDefinition;
import org.openmrs.module.appointments.model.AppointmentServiceType;
import org.openmrs.module.appointments.model.AppointmentStatus;
import org.openmrs.module.appointments.model.Speciality;
import org.openmrs.module.appointments.service.AppointmentServiceDefinitionService;
import org.openmrs.module.appointments.service.SpecialityService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ManageAppointmentsTypesFragmentController {
	
	public void controller(FragmentModel model) {
		SpecialityService specialityService = Context.getService(SpecialityService.class);
		AppointmentServiceDefinitionService appointmentServiceDefinitionService = Context
		        .getService(AppointmentServiceDefinitionService.class);
		List<AppointmentServiceDefinition> appointmentServiceList = new ArrayList<AppointmentServiceDefinition>(
		        appointmentServiceDefinitionService.getAllAppointmentServices(false));
		
		model.addAttribute("specialityTypes", specialityService.getAllSpecialities());
		model.addAttribute("appointmentService", appointmentServiceList);
	}
	
	public String createAppointmentService(@RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "description", required = false) String description,
	        @RequestParam(value = "speciality", required = false) String specialityUuid,
	        @RequestParam(value = "hourStartTime", required = false) String hourStartTime,
	        @RequestParam(value = "minutesStartTime", required = false) String minutesStartTime,
	        @RequestParam(value = "hourEndTime", required = false) String hourEndTime,
	        @RequestParam(value = "minutesEndTime", required = false) String minutesEndTime,
	        @RequestParam(value = "maxAppointmentsLimit", required = false) Integer maxAppointmentsLimit,
	        @RequestParam(value = "durationMins", required = false) Integer durationMins,
	        @RequestParam(value = "initialAppointmentStatus", required = false) Integer initialAppointmentStatus) {
		
		AppointmentServiceDefinitionService appointmentServiceDefinitionService = Context
		        .getService(AppointmentServiceDefinitionService.class);
		AppointmentServiceDefinition appointmentServiceDefinition = new AppointmentServiceDefinition();
		SpecialityService specialityService = Context.getService(SpecialityService.class);
		if (StringUtils.isNotBlank(name)) {
			appointmentServiceDefinition.setName(name);
			if (StringUtils.isNotBlank(description)) {
				appointmentServiceDefinition.setDescription(description);
			}
			if (StringUtils.isNotBlank(specialityUuid)) {
				appointmentServiceDefinition.setSpeciality(specialityService.getSpecialityByUuid(specialityUuid));
			}
			if (StringUtils.isNotBlank(hourStartTime) && StringUtils.isNotBlank(minutesStartTime)) {
				String startTime = hourStartTime + ":" + minutesStartTime + ":00";
				LocalTime time = LocalTime.parse(startTime);
				System.out.println("The start time is >>" + startTime);
				appointmentServiceDefinition.setStartTime(Time.valueOf(time));
			}
			if (StringUtils.isNotBlank(hourEndTime) && StringUtils.isNotBlank(minutesEndTime)) {
				String endTime = hourEndTime + ":" + minutesEndTime + ":00";
				LocalTime time = LocalTime.parse(endTime);
				appointmentServiceDefinition.setEndTime(Time.valueOf(time));
			}
			if (maxAppointmentsLimit != null) {
				appointmentServiceDefinition.setMaxAppointmentsLimit(maxAppointmentsLimit);
			}
			if (durationMins != null) {
				appointmentServiceDefinition.setDurationMins(durationMins);
			}
			if (initialAppointmentStatus != null) {
				appointmentServiceDefinition.setInitialAppointmentStatus(AppointmentStatus.Requested);
			}
			appointmentServiceDefinition.setCreator(Context.getAuthenticatedUser());
			appointmentServiceDefinition.setDateCreated(new Date());
			appointmentServiceDefinition.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
			appointmentServiceDefinition.setUuid(UUID.randomUUID().toString());
			//save the appointment service
			appointmentServiceDefinitionService.save(appointmentServiceDefinition);
		}
		return "Appointment type created";
	}
	
	public String editAppointmentType(
	        @RequestParam(value = "appointmentTypeId", required = false) Integer appointmentTypeId,
	        @RequestParam(value = "editName", required = false) String editName,
	        @RequestParam(value = "editAppointmentVisitType") VisitType editAppointmentVisitType,
	        @RequestParam(value = "editAppointmentDuration", required = false) Integer editAppointmentDuration,
	        @RequestParam(value = "editDescription", required = false) String editDescription,
	        @RequestParam(value = "editAction", required = false) Integer editAction,
	        @RequestParam(value = "editAppointmentRetire", required = false) String editAppointmentRetire) {
		
		AppointmentServiceDefinitionService appointmentServiceDefinitionService = Context
		        .getService(AppointmentServiceDefinitionService.class);
		
		AppointmentServiceDefinition appointmentServiceDefinition = appointmentServiceDefinitionService
		        .getAppointmentServiceByUuid("");
		/*ehrAppointmentType.setName(editName);
		ehrAppointmentType.setVisitType(editAppointmentVisitType);
		ehrAppointmentType.setDuration(editAppointmentDuration);
		ehrAppointmentType.setDescription(editDescription);*/
		
		/*if (editAction != null) {
			if (editAction == 1) {
				ehrAppointmentService.saveEhrAppointmentType(ehrAppointmentType);
			} else if (editAction == 2 && StringUtils.isNotBlank(editAppointmentRetire)) {
				ehrAppointmentType.setRetired(true);
				ehrAppointmentType.setRetiredBy(Context.getAuthenticatedUser());
				ehrAppointmentType.setRetireReason(editAppointmentRetire);
				ehrAppointmentService.saveEhrAppointmentType(ehrAppointmentType);
			} else if (editAction == 3) {
				ehrAppointmentService.purgeEhrAppointmentType(ehrAppointmentType);
			}
		}*/
		return "Appointment type edited";
	}
	
	public String createSpecialityType(@RequestParam(value = "specialityName", required = false) String specialityName) {
		
		SpecialityService specialityService = Context.getService(SpecialityService.class);
		Speciality speciality = new Speciality();
		if (StringUtils.isNotBlank(specialityName)) {
			speciality.setName(specialityName);
			speciality.setCreator(Context.getAuthenticatedUser());
			speciality.setDateCreated(new Date());
			speciality.setUuid(UUID.randomUUID().toString());
			//save the speciality type
			specialityService.save(speciality);
		}
		
		return "Speciality type created";
	}
	
	public String createAppointmentServiceType(@RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "duration") Integer duration,
	        @RequestParam(value = "appointmentServiceDefinition", required = false) String appointmentServiceDefinition) {
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		AppointmentServiceDefinitionService appointmentServiceDefinitionService = Context
		        .getService(AppointmentServiceDefinitionService.class);
		AppointmentServiceType appointmentServiceType = new AppointmentServiceType();
		if (StringUtils.isNotBlank(name)) {
			appointmentServiceType.setName(name);
			appointmentServiceType.setCreator(Context.getAuthenticatedUser());
			appointmentServiceType.setDuration(duration);
			appointmentServiceType.setAppointmentServiceDefinition(appointmentServiceDefinitionService
			        .getAppointmentServiceByUuid(appointmentServiceDefinition));
			appointmentServiceType.setDateCreated(new Date());
			appointmentServiceType.setUuid(UUID.randomUUID().toString());
			
			//save the appointment service type
			hospitalCoreService.saveAppointmentServiceType(appointmentServiceType);
		}
		return "Created appointment service type";
		
	}
	
}
