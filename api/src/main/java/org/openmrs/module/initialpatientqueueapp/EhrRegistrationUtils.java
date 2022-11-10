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
import org.openmrs.Visit;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
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
import java.util.List;
import java.util.Random;

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
	
	public static Date requiredDate(Date date, Integer weight) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, weight);
		return calendar.getTime();
		
	}
	
	public static Date getPreviousVisitDate(Patient patient) {
		List<Visit> visitList = Context.getVisitService().getVisitsByPatient(patient);
		List<Visit> filteredVisits = new ArrayList<Visit>();
		Date previousVisitDate = null;
		
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
	
}
