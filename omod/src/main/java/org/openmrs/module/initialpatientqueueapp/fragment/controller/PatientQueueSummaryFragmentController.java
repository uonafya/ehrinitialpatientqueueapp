package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ehrconfigs.metadata.EhrCommonMetadata;
import org.openmrs.parameter.EncounterSearchCriteria;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PatientQueueSummaryFragmentController {
	
	public void controller(FragmentModel model, @RequestParam(value = "patientId", required = false) Patient patient) {
		EncounterService encounterService = Context.getEncounterService();
		List<Encounter> filtered = new ArrayList<Encounter>();
		List<Encounter> patientEncounters = encounterService.getEncounters(new EncounterSearchCriteria(patient, null, null,
		        null, null, null, Arrays.asList(
		            encounterService.getEncounterTypeByUuid(EhrCommonMetadata._EhrEncounterTypes.REGINITIAL),
		            encounterService.getEncounterTypeByUuid(EhrCommonMetadata._EhrEncounterTypes.REGREVISIT)), null, null,
		        null, false));
		if (!patientEncounters.isEmpty() && patientEncounters.size() > 5) {
			//filtered = //get the first 5
			filtered = patientEncounters.stream().limit(5).collect(Collectors.toList());
		} else {
			filtered = patientEncounters;
			
		}
		model.addAttribute("encounters", filtered);
	}
}
