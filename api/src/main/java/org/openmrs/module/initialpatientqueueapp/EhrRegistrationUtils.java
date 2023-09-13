/**
 *  Copyright 2008 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Registration module.
 *
 *  Registration module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Registration module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Registration module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package org.openmrs.module.initialpatientqueueapp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.openmrs.Visit;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.EhrAppointmentService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.EhrAppointmentBlock;
import org.openmrs.module.hospitalcore.model.EhrAppointmentType;
import org.openmrs.module.hospitalcore.model.EhrTimeSlot;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.hospitalcore.util.DateUtils;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.hospitalcore.util.PatientUtils;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EhrRegistrationUtils {
	
	private static Log logger = LogFactory.getLog(EhrRegistrationUtils.class);
	
	/**
	 * Parse Date
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.parse(date);
	}
	
	/**
	 * Format date
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}
	
	public static String formatDateTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}
	
	public static String formatDateAs(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	/**
	 * Generate person name
	 * 
	 * @param personName TODO
	 * @param firstName
	 * @return
	 */
	public static PersonName getPersonName(PersonName personName, String firstName, String otherName, String surName) {
		
		if (personName == null)
			personName = new PersonName();
		
		//personName.setFamilyNamePrefix(surName);
		personName.setFamilyName(surName);
		personName.setGivenName(firstName);
		if (!StringUtils.isBlank(otherName)) {
			personName.setMiddleName(otherName);
		} else {
			personName.setMiddleName("");
		}
		
		personName.setPreferred(true);
		return personName;
	}
	
	/**
	 * Generate patient identifier
	 * 
	 * @param identifier
	 * @return
	 */
	public static PatientIdentifier getPatientIdentifier(String identifier) {
		Location location = new Location(GlobalPropertyUtil.getInteger(InitialPatientQueueConstants.PROPERTY_LOCATION, 1));
		PatientIdentifierType identType = Context.getPatientService().getPatientIdentifierType(
		    GlobalPropertyUtil.getInteger(InitialPatientQueueConstants.PROPERTY_PATIENT_IDENTIFIER_TYPE, 1));
		PatientIdentifier patientIdentifier = new PatientIdentifier(identifier, identType, location);
		return patientIdentifier;
	}
	
	/**
	 * Creates a new Patient Identifier: <prefix>YYMMDDhhmmxxx-checkdigit where prefix = global_prop
	 * (registration.identifier_prefix) YY = two char representation of current year e.g. 2009 - 09
	 * MM = current month. e.g. January - 1; December - 12 DD = current day of month e.g. 20 hh =
	 * hour of day e.g. 10PM - 22 mm = minustes e.g. 10:12 - 12 xxx = three random digits e.g. from
	 * 0 - 999 checkdigit = using the Lunh Algorithm
	 */
	public static String getNewIdentifier() {
		Calendar now = Calendar.getInstance();
		String shortName = GlobalPropertyUtil.getString(InitialPatientQueueConstants.PROPERTY_IDENTIFIER_PREFIX, "");
		String noCheck = shortName + String.valueOf(now.get(Calendar.YEAR)).substring(2, 4)
		        + String.valueOf(now.get(Calendar.MONTH) + 1) + String.valueOf(now.get(Calendar.DATE))
		        //Sagar Bele,Ghanshyam Kumar - 12-12-2012 - Bug #467 [Registration]Duplicate Identifier
		        + String.valueOf(now.get(Calendar.HOUR)) + String.valueOf(now.get(Calendar.MINUTE))
		        + String.valueOf(now.get(Calendar.SECOND))
		        //Sagar Bele,Ghanshyam Kumar - 12-12-2012 - Bug #467 [Registration]Duplicate Identifier
		        //ghanshyam  18-may-2013 #1647 Id generated by the system should be a fixed length string
		        + String.valueOf(new Random().nextInt(9999 - 999 + 1));
		return noCheck + "-" + generateCheckdigit(noCheck);
	}
	
	/*
	 * Using the Luhn Algorithm to generate check digits
	 *
	 * @param idWithoutCheckdigit
	 *
	 * @return idWithCheckdigit
	 */
	private static int generateCheckdigit(String input) {
		int factor = 2;
		int sum = 0;
		int n = 10;
		int length = input.length();
		
		if (!input.matches("[\\w]+"))
			throw new RuntimeException("Invalid character in patient id: " + input);
		// Work from right to left
		for (int i = length - 1; i >= 0; i--) {
			int codePoint = input.charAt(i) - 48;
			// slight openmrs peculiarity to Luhn's algorithm
			int accum = factor * codePoint - (factor - 1) * (int) (codePoint / 5) * 9;
			
			// Alternate the "factor"
			factor = (factor == 2) ? 1 : 2;
			
			sum += accum;
		}
		
		int remainder = sum % n;
		return (n - remainder) % n;
	}
	
	/**
	 * Get person address
	 * 
	 * @param address TODO
	 * @param postalAddress
	 * @param district
	 * @param upazila
	 * @return
	 */
	public static PersonAddress getPersonAddress(PersonAddress address, String postalAddress, String district,
	        String upazila, String location) {
		
		if (address == null)
			address = new PersonAddress();
		
		address.setAddress1(postalAddress);
		address.setCountyDistrict(district);
		address.setCityVillage(upazila);
		address.setAddress2(location);
		
		return address;
	}
	
	/**
	 * Get person attribute
	 * 
	 * @param id
	 * @param value
	 * @return
	 */
	public static PersonAttribute getPersonAttribute(Integer id, String value) {
		PersonAttributeType type = Context.getPersonService().getPersonAttributeType(id);
		PersonAttribute attribute = new PersonAttribute();
		attribute.setAttributeType(type);
		attribute.setValue(value);
		logger.info(String.format("Saving new person attribute [name=%s, value=%s]", type.getName(), value));
		return attribute;
	}
	
	/**
	 * Estimate age using birthdate
	 * 
	 * @param birthdate
	 * @return
	 * @throws ParseException
	 */
	public static String estimateAge(String birthdate) throws ParseException {
		Date date = parseDate(birthdate);
		return PatientUtils.estimateAge(date);
	}
	
	public static String estimateAgeInYear(String birthdate) throws ParseException {
		Date date = parseDate(birthdate);
		return PatientUtils.estimateAgeInYear(date);
	}
	
	/**
	 * Save common information to patientSearch table to speed up search process
	 * 
	 * @param patient
	 */
	public static void savePatientSearch(Patient patient) {
		PatientSearch ps = new PatientSearch();
		String fullname = PatientUtils.getFullName(patient);
		ps.setFullname(fullname);
		ps.setPatientId(patient.getPatientId());
		ps.setAge(patient.getAge());
		ps.setBirthdate(patient.getBirthdate());
		ps.setGender(patient.getGender());
		ps.setFamilyName(patient.getFamilyName());
		ps.setGivenName(patient.getGivenName());
		ps.setMiddleName(patient.getMiddleName());
		ps.setIdentifier(patient.getPatientIdentifier().getIdentifier());
		ps.setPersonNameId(patient.getPersonName().getId());
		
		Context.getService(HospitalCoreService.class).savePatientSearch(ps);
	}
	
	private static List<Obs> getLastSpecialClinicVisitForPatient(Person patient) {
		ObsService obsService = Context.getObsService();
		Concept specialClinic = Context.getConceptService().getConceptByUuid("b5e0cfd3-1009-4527-8e36-83b5e902b3ea");
		Location defaultLocation = Context.getService(KenyaEmrService.class).getDefaultLocation();
		return obsService.getObservations(Arrays.asList(patient), null, Arrays.asList(specialClinic), null, null,
		    Arrays.asList(defaultLocation), null, null, null, null, null, false);
	}
	
	public static boolean hasSpecialClinicVisit(Person person) {
		boolean hasSecondSpecialClinicVisit = false;
		
		if (getLastSpecialClinicVisitForPatient(person).size() > 1) {
			hasSecondSpecialClinicVisit = true;
		}
		return hasSecondSpecialClinicVisit;
	}
	
	/**
	 * Get the date based on the value passed for the manipulation
	 * 
	 * @param date
	 * @param weight
	 * @return
	 */
	public static Date requiredDate(Date date, Integer units, Integer weight) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(units, weight);
		return calendar.getTime();
		
	}
	
	public static Date getPreviousVisitDate(Patient patient) {
		List<Visit> visitList = Context.getVisitService().getVisitsByPatient(patient);
		List<Visit> filteredVisits = new ArrayList<Visit>();
		Date previousVisitDate = DateUtils.getStartOfDay(new Date());
		
		Date startDateToday = DateUtils.getStartOfDay(new Date());
		//remove today's visit from a list of visits
		for (Visit visit : visitList) {
			if (!(visit.getStartDatetime().compareTo(startDateToday) >= 0)) {
				filteredVisits.add(visit);
			}
		}
		if (!filteredVisits.isEmpty()) {
			orderedList(filteredVisits);
			previousVisitDate = filteredVisits.get(filteredVisits.size() - 1).getStartDatetime();
		}
		return previousVisitDate;
		
	}
	
	public static List<Visit> orderedList(List<Visit> visitList) {
		//sort them with the most recent one at the bottom
		Collections.sort(visitList, new Comparator<Visit>() {
			
			public int compare(Visit v1, Visit v2) {
				if (v1.getStartDatetime() == null || v2.getStartDatetime() == null)
					return 0;
				return v1.getStartDatetime().compareTo(v2.getStartDatetime());
			}
		});
		return visitList;
	}
	
	public static Date formatDateFromStringWithTime(String date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
	}
	
	public static EhrTimeSlot getAppointmentTimeSlot(Date startDate, Date endDate, Provider provider, Location location,
	        EhrAppointmentType type) {
		EhrTimeSlot timeSlot = new EhrTimeSlot();
		timeSlot.setStartDate(startDate);
		timeSlot.setEndDate(endDate);
		timeSlot.setCreator(Context.getAuthenticatedUser());
		timeSlot.setDateCreated(new Date());
		
		//add an appointment block
		timeSlot.setAppointmentBlock(getAppointmentBlock(startDate, endDate, provider, location, type));
		
		return timeSlot;
	}
	
	public static EhrAppointmentBlock getAppointmentBlock(Date startDate, Date endDate, Provider provider,
	        Location location, EhrAppointmentType type) {
		Set<EhrAppointmentType> appointmentTypeSet = new HashSet<EhrAppointmentType>();
		appointmentTypeSet.add(type);
		
		EhrAppointmentBlock appointmentBlock = new EhrAppointmentBlock();
		appointmentBlock.setStartDate(startDate);
		appointmentBlock.setEndDate(endDate);
		appointmentBlock.setProvider(provider);
		appointmentBlock.setLocation(location);
		appointmentBlock.setTypes(appointmentTypeSet);
		appointmentBlock.setCreator(Context.getAuthenticatedUser());
		appointmentBlock.setDateCreated(new Date());
		return appointmentBlock;
	}
	
	public static EhrAppointmentType getDefaultAppointmentType() {
		EhrAppointmentService service = Context.getService(EhrAppointmentService.class);
		EhrAppointmentType appointmentType = service
		        .getEhrAppointmentTypeByUuid("BotswanaEmrConstants.REGULAR_FOLLOW_UP_APPOINTMENT_TYPE");
		if (appointmentType == null) {
			appointmentType = createDefaultAppointmentType(service);
		}
		return appointmentType;
	}
	
	private static EhrAppointmentType createDefaultAppointmentType(EhrAppointmentService service) {
		EhrAppointmentType appointmentType = new EhrAppointmentType();
		appointmentType.setUuid("BotswanaEmrConstants.REGULAR_FOLLOW_UP_APPOINTMENT_TYPE");
		appointmentType.setName("Regular follow up appointment");
		appointmentType.setDescription("Regular follow up appointment type");
		appointmentType.setDuration(10);
		
		return service.saveEhrAppointmentType(appointmentType);
	}
	
	/**
	 * Calculates the days since the given date
	 * 
	 * @param date1 the date
	 * @param date2 the date2
	 * @return the number of days
	 */
	public static String unitsSince(Date date1, Date date2) {
		int valueHours = 0;
		int valueMinutes = 0;
		int valueSeconds = 0;
		String diff = "";
		DateTime d1 = new DateTime(date1.getTime());
		DateTime d2 = new DateTime(date2.getTime());
		valueHours = Math.abs(Hours.hoursBetween(d1, d2).getHours());
		valueMinutes = Math.abs(Minutes.minutesBetween(d1, d2).getMinutes());
		valueSeconds = Math.abs(Seconds.secondsBetween(d1, d2).getSeconds());
		if (valueHours > 0) {
			diff = valueHours + " Hrs";
		} else if (valueHours == 0 && valueMinutes > 0) {
			diff = valueMinutes + " Min";
		} else if (valueHours == 0 && valueMinutes == 0 && valueSeconds > 0) {
			diff = valueSeconds + " Sec";
		}
		
		return diff;
	}
	
	public static int unitsSinceInteger(Date date1, Date date2, String type) {
		int value = 0;
		DateTime d1 = new DateTime(date1.getTime());
		DateTime d2 = new DateTime(date2.getTime());
		if (type.equals("days")) {
			value = Math.abs(Days.daysBetween(d1, d2).getDays());
		} else if (type.equals("hours")) {
			value = Math.abs(Hours.hoursBetween(d1, d2).getHours());
		} else if (type.equals("minutes")) {
			value = Math.abs(Minutes.minutesBetween(d1, d2).getMinutes());
		} else if (type.equals("seconds")) {
			value = Math.abs(Seconds.secondsBetween(d1, d2).getSeconds());
		} else if (type.equals("years")) {
			value = Math.abs(Years.yearsBetween(d1, d2).getYears());
		} else if (type.equals("weeks")) {
			value = Math.abs(Weeks.weeksBetween(d1, d2).getWeeks());
		} else if (type.equals("months")) {
			value = Math.abs(Months.monthsBetween(d1, d2).getMonths());
		}
		return value;
	}
	
}
