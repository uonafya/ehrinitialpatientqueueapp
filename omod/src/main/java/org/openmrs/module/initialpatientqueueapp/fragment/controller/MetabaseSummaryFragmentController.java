package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.Patient;
import org.openmrs.module.initialpatientqueueapp.Utils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class MetabaseSummaryFragmentController {
	
	public void controller(FragmentModel model, @RequestParam(value = "patientId", required = false) Patient patient) {
		model.addAttribute("url", Utils.getDashBoards(103, patient.getPatientId()));
	}
}
