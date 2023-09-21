package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientSummaryFragmentController {
	
	public void controller(@FragmentParam("patient") Patient patient, @SpringBean KenyaUiUtils kenyaUi,
	        PageRequest pageRequest, UiUtils ui, FragmentModel model) {
		
		AppDescriptor currentApp = kenyaUi.getCurrentApp(pageRequest);
		
		// Get all common per-patient forms as simple objects
		List<SimpleObject> forms = new ArrayList<SimpleObject>();
		
		model.addAttribute("patient", patient);
		model.addAttribute("attributes", personAttributeList(patient));
	}
	
	private List<PersonAttribute> personAttributeList(Patient patient) {
		
		List<PersonAttribute> personAttributeList = new ArrayList<PersonAttribute>();
		if (patient.getAttributes() != null) {
			for (PersonAttribute personAttribute : patient.getAttributes()) {
				if (!(getRelevantPersonAttributeTypes().contains(personAttribute.getAttributeType()))
				        && StringUtils.isNotBlank(personAttribute.getValue())) {
					personAttributeList.add(personAttribute);
				}
			}
		}
		
		return personAttributeList;
	}
	
	private List<PersonAttributeType> getRelevantPersonAttributeTypes() {
		PersonService personService = Context.getPersonService();
		return Arrays.asList(personService.getPersonAttributeTypeByUuid("4dfa195f-8420-424d-8275-d60cf115303d"),
		    personService.getPersonAttributeTypeByUuid("9bc43f7e-ff05-4afb-8dc4-710d245a927c"));
	}
	
}
