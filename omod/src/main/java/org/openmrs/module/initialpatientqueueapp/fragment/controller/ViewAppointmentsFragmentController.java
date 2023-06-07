package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.hospitalcore.model.EhrAppointmentSimplifier;
import org.openmrs.module.hospitalcore.model.EhrAppointmentType;
import org.openmrs.module.hospitalcore.model.EhrTimeSlot;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils.getDefaultAppointmentType;

@Slf4j
public class ViewAppointmentsFragmentController {
	
	public void controller(@SpringBean FragmentModel model,
	        @RequestParam(required = false, value = "scheduledDate") Date scheduledDate) {
		EhrAppointmentService appointmentService = Context.getService(EhrAppointmentService.class);
		
		// Get the date for schedule view
		if (scheduledDate == null) {
			scheduledDate = new Date();
		}
		List<EhrAppointment> getTodaysAppointments = new ArrayList<EhrAppointment>(
		        appointmentService.getEhrAppointmentsByConstraints(DateUtil.getStartOfDay(scheduledDate),
		            DateUtil.getEndOfDay(scheduledDate), null, null, null, null));
		
		EhrAppointmentSimplifier ehrAppointmentSimplifier;
		List<EhrAppointmentSimplifier> simplifierList = new ArrayList<EhrAppointmentSimplifier>();
		for (EhrAppointment ehrAppointment : getTodaysAppointments) {
			ehrAppointmentSimplifier = new EhrAppointmentSimplifier();
			ehrAppointmentSimplifier.setAppointmentType(ehrAppointment.getAppointmentType().getName());
			ehrAppointmentSimplifier.setPatient(ehrAppointment.getPatient());
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
		model.addAttribute("getTodaysAppointments", simplifierList);
	}
	
	public String createAppointment(@RequestParam(value = "appointmentDate", required = false) String appointmentDate,
	        @RequestParam(value = "startTime", required = false) String startTime,
	        @RequestParam(value = "endTime", required = false) String endTime, @RequestParam(value = "type") String type,
	        @RequestParam(value = "flow", required = false) String flow,
	        @RequestParam(value = "patientId", required = false) Patient patient,
	        @RequestParam(value = "notes", required = false) String notes) throws ParseException {
		Integer locationId = Context.getService(KenyaEmrService.class).getDefaultLocation().getLocationId();
		EhrAppointmentService appointmentService = Context.getService(EhrAppointmentService.class);
		LocationService locationService = Context.getLocationService();
		Provider provider = HospitalCoreUtils.getProvider(Context.getAuthenticatedUser().getPerson());
		
		EhrAppointment appointment = new EhrAppointment();
		appointment.setDateCreated(new Date());
		
		if (patient == null && type.equals("Task")) {
			//Fake appointment patient
			appointment.setPatient(getAppointmentTaskPatient());
		} else {
			appointment.setPatient(patient);
		}
		
		if (StringUtils.isNotBlank(type)) {
			appointment.setAppointmentType(getUserDefinedAppointmentType(type));
		}
		appointment.setDateCreated(new Date());
		appointment.setCreator(Context.getAuthenticatedUser());
		
		if (StringUtils.isNotBlank(notes)) {
			appointment.setReason(notes);
		}
		if (StringUtils.isNotBlank(flow)) {
			appointment.setStatus(EhrAppointment.EhrAppointmentStatus.WALKIN);
			//Start a new visit
			VisitType facilityVisitType = Context.getVisitService().getVisitTypeByUuid("");
			
			Visit visit = new Visit(patient, facilityVisitType, new Date());
			visit.setLocation(locationService.getLocation(locationId));
			visit = Context.getVisitService().saveVisit(visit);
			appointment.setVisit(visit);
		} else {
			//add this time slot to the appointment
			if (StringUtils.isNotBlank(appointmentDate) && StringUtils.isNotBlank(startTime)
			        && StringUtils.isNotBlank(endTime)) {
				String startDateStr = appointmentDate + " " + startTime;
				String endDateStr = appointmentDate + " " + endTime;
				Date startDate = EhrRegistrationUtils.formatDateFromStringWithTime(startDateStr);
				Date endDate = EhrRegistrationUtils.formatDateFromStringWithTime(endDateStr);
				
				EhrTimeSlot appointmentTimeSlot = EhrRegistrationUtils.getAppointmentTimeSlot(
				    startDate,
				    endDate,
				    provider,
				    locationId == null ? Context.getService(KenyaEmrService.class).getDefaultLocation() : locationService
				            .getLocation(locationId), getUserDefinedAppointmentType(type));
				
				appointmentService.saveEhrAppointmentBlock(appointmentTimeSlot.getAppointmentBlock());
				appointmentService.saveEhrTimeSlot(appointmentTimeSlot);
				appointment.setTimeSlot(appointmentTimeSlot);
				appointment.setStatus(EhrAppointment.EhrAppointmentStatus.SCHEDULED);
				appointmentService.saveEhrAppointment(appointment);
			}
		}
		return null;
	}
	
	private EhrAppointmentType getUserDefinedAppointmentType(@NonNull String type) {
		if (type.equals("Task")) {
			//task appointment type
			return getTaskAppointmentType();
		} else {
			// follow-up appointment
			return getDefaultAppointmentType();
		}
	}
	
	private Patient getAppointmentTaskPatient() {
		String patientUuid1 = Context.getPatientService().getPatient(1).getPerson().getUuid();
		System.out.println("uuid one is >>" + patientUuid1);
		
		String patientUuid2 = Context.getPatientService().getAllPatients().get(0).getPerson().getUuid();
		
		Patient fakePatient = Context.getPatientService().getPatientByUuid(patientUuid1);
		System.out.println("uuid two is >>" + patientUuid2);
		
		if (fakePatient == null) {
			fakePatient = Context.getPatientService().getPatientByUuid(patientUuid2);
		}
		System.out.println("We got this patient out >>" + fakePatient);
		return fakePatient;
	}
	
	private EhrAppointmentType getTaskAppointmentType() {
		EhrAppointmentService service = Context.getService(EhrAppointmentService.class);
		EhrAppointmentType appointmentType = service.getEhrAppointmentTypeByUuid("3304E4C0-620C-483F-94EB-BD67E730CD18");
		if (appointmentType == null) {
			appointmentType = createTaskAppointmentType(service);
		}
		return appointmentType;
	}
	
	private EhrAppointmentType createTaskAppointmentType(EhrAppointmentService service) {
		EhrAppointmentType taskAppointmentType = new EhrAppointmentType();
		taskAppointmentType.setUuid("3304E4C0-620C-483F-94EB-BD67E730CD18");
		taskAppointmentType.setName("Normal appointment");
		taskAppointmentType.setDescription("Regular appointment type used in a facility");
		taskAppointmentType.setDuration(30);
		
		return service.saveEhrAppointmentType(taskAppointmentType);
	}
	
	public List<EhrAppointment> getAppointmentsByLoggedInProvider() {
		User user = Context.getUserContext().getAuthenticatedUser();
		Provider provider = null;
		if (user != null) {
			provider = HospitalCoreUtils.getProvider(user.getPerson());
		}
		
		//Lower limit starts from yesterday to include today's events
		Instant now = Instant.now();
		Instant yesterday = now.minus(1, ChronoUnit.DAYS);
		
		return new ArrayList<EhrAppointment>(Context.getService(EhrAppointmentService.class)
		        .getEhrAppointmentsByConstraints(Date.from(yesterday), null, null, provider, null, null));
	}
	
	public List<ScheduleFragmentController.Event> getCalenderEventsForProvider() {
		List<ScheduleFragmentController.Event> events = new ArrayList<ScheduleFragmentController.Event>();
		for (EhrAppointment appointment : this.getAppointmentsByLoggedInProvider()) {
			ScheduleFragmentController.Event event = new ScheduleFragmentController.Event();
			if (appointment.getAppointmentType().getUuid().equals("3304E4C0-620C-483F-94EB-BD67E730CD18")) {
				event.setTitle(appointment.getReason());
			} else {
				event.setTitle(appointment.getPatient().getPerson().getPersonName().getFullName());
			}
			event.setStartDate(appointment.getTimeSlot().getStartDate());
			event.setEndDate(appointment.getTimeSlot().getEndDate());
			events.add(event);
		}
		return events;
	}
	
}
