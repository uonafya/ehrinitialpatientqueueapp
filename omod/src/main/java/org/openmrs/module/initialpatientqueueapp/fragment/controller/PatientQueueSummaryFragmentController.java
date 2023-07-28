package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ehrconfigs.metadata.EhrCommonMetadata;
import org.openmrs.parameter.EncounterSearchCriteria;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;
import org.openmrs.module.patientdashboardapp.model.VisitDetail;

import javax.tools.SimpleJavaFileObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
			//order by date then filter the first 5
			filtered = patientEncounters.subList(0, 5);
		} else {
			filtered = patientEncounters;
			
		}
		model.addAttribute("encounters", filtered);
		model.addAttribute("patient", patient);
		model.addAttribute("userLocation", Context.getService(KenyaEmrService.class).getDefaultLocation().getName());
	}
	
}
