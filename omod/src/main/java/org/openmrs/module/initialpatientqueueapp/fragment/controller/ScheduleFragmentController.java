package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.model.EhrAppointment;
import org.openmrs.module.hospitalcore.model.EhrAppointmentType;
import org.openmrs.module.hospitalcore.model.EhrTimeSlot;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils.getDefaultAppointmentType;

@Slf4j
public class ScheduleFragmentController {
	
	public void controller(@SpringBean FragmentModel model,
	        @RequestParam(value = "patientId", required = false) Patient patient) {
		//model.addAttribute("providerList", getProviderList());
		model.addAttribute("appointmentTypes", getAppointmentTypeList());
		model.addAttribute("location", Context.getService(KenyaEmrService.class).getDefaultLocation());
		model.addAttribute("patient", patient);
		model.addAttribute("events", this.getCalenderEventsForProvider());
	}
	
	public String createAppointment(@RequestParam(value = "appointmentDate", required = false) String appointmentDate,
	        @RequestParam(value = "startTime", required = false) String startTime,
	        @RequestParam(value = "endTime", required = false) String endTime,
	        @RequestParam(value = "flow", required = false) String flow,
	        @RequestParam(value = "patientId", required = false) Patient patient,
	        @RequestParam(value = "notes", required = false) String notes, Integer locationId) throws ParseException {
		
		EhrAppointmentService appointmentService = Context.getService(EhrAppointmentService.class);
		LocationService locationService = Context.getLocationService();
		Provider provider = HospitalCoreUtils.getProvider(Context.getAuthenticatedUser().getPerson());
		EhrAppointment appointment = new EhrAppointment();
		appointment.setDateCreated(new Date());
		appointment.setPatient(patient);
		EhrAppointmentType defaultAppointmentType = getDefaultAppointmentType();
		if (defaultAppointmentType != null) {
			appointment.setAppointmentType(defaultAppointmentType);
		}
		appointment.setDateCreated(new Date());
		appointment.setCreator(Context.getAuthenticatedUser());
		
		if (StringUtils.isNotBlank(notes)) {
			appointment.setReason(notes);
		}
		if (StringUtils.isNotBlank(flow)) {
			appointment.setStatus(EhrAppointment.EhrAppointmentStatus.WALKIN);
			//Start a new visit
			VisitType facilityVisitType = Context.getVisitService().getVisitTypeByUuid(
			    "BotswanaEmrConstants.FACILITY_VISIT_VISIT_TYPE_UUID");
			
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
				            .getLocation(locationId), defaultAppointmentType);
				appointmentService.saveEhrAppointmentBlock(appointmentTimeSlot.getAppointmentBlock());
				appointmentService.saveEhrTimeSlot(appointmentTimeSlot);
				appointment.setTimeSlot(appointmentTimeSlot);
				appointment.setStatus(EhrAppointment.EhrAppointmentStatus.SCHEDULED);
				appointmentService.saveEhrAppointment(appointment);
			}
		}
		return null;
	}
	
	public EhrAppointment getAppointmentForPatient(
	        @RequestParam(value = "appointmentId", required = false) Integer appointmentId,
	        @RequestParam(value = "patientId", required = false) Integer patientId) {
		EhrAppointment appointment = null;
		
		EhrAppointmentService as = Context.getService(EhrAppointmentService.class);
		if (appointmentId != null)
			appointment = as.getEhrAppointment(appointmentId);
		
		if (appointment == null) {
			appointment = new EhrAppointment();
			if (patientId != null)
				appointment.setPatient(Context.getPatientService().getPatient(patientId));
		}
		
		return appointment;
	}
	
	public List<EhrAppointmentType> getAppointmentTypeList() {
		return Context.getService(EhrAppointmentService.class).getAllEhrAppointmentTypesSorted(false);
	}
	
	public List<Provider> getProviderList() {
		return Context.getService(EhrAppointmentService.class).getAllEhrProvidersSorted(false);
	}
	
	public List<EhrAppointment> getAppointmentsByLoggedInProvider() {
		Provider provider = HospitalCoreUtils.getProvider(Context.getAuthenticatedUser().getPerson());
		return new ArrayList<EhrAppointment>(Context.getService(EhrAppointmentService.class)
		        .getEhrAppointmentsByConstraints(new Date(), null, null, provider, null, null));
	}
	
	public List<Event> getCalenderEventsForProvider() {
		List<Event> events = new ArrayList<Event>();
		for (EhrAppointment appointment : this.getAppointmentsByLoggedInProvider()) {
			events.add(new Event(appointment.getPatient().getPerson().getPersonName().getFullName() + " "
			        + (appointment.getReason() != null ? appointment.getReason() : ""), appointment.getTimeSlot()
			        .getStartDate(), appointment.getTimeSlot().getEndDate()));
		}
		return events;
	}
	
	public List<SimpleObject> getAppointmentByTimeSlotAndProvider(@RequestParam("fromDate") Date startDate,
	        @RequestParam("toDate") Date toDate, @RequestParam("provider") Provider provider, UiUtils ui) {
		EhrAppointmentService appointmentService = Context.getService(EhrAppointmentService.class);
		
		List<EhrAppointment> appointmentList = new ArrayList<EhrAppointment>(
		        appointmentService.getEhrAppointmentsByConstraints(startDate, toDate, null, provider, null, null));
		return SimpleObject.fromCollection(appointmentList, ui, "id", "patient", "status", "appointmentType");
		
	}
	
	/*
	 *This method is intended to be used when a scheduled appointment is clicked on the calendar
	 * The dialog box that will pop up will have several appointment status that can be adjusted to
	 *
	 */
	public String editAppointments(@RequestParam("appointmentId") EhrAppointment appointment,
	        @RequestParam("appointmentStatusId") EhrAppointment.EhrAppointmentStatus appointmentStatus) {
		
		EhrAppointmentService appointmentService = Context.getService(EhrAppointmentService.class);
		if (appointment != null) {
			appointmentService.changeEhrAppointmentStatus(appointment, appointmentStatus);
			
		}
		return null;
	}
	
	public String deleteAppointments(@RequestParam("appointmentId") EhrAppointment appointment,
	        @RequestParam("appointmentStatusId") EhrAppointment.EhrAppointmentStatus appointmentStatus) {
		EhrAppointmentService appointmentService = Context.getService(EhrAppointmentService.class);
		if (appointment != null) {
			appointmentService.purgeEhrAppointment(appointment);
			
		}
		
		return null;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Event implements Serializable {
		
		private String title;
		
		private Date startDate;
		
		private Date endDate;
	}
}
