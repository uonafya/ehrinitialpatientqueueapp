package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.SickOff;
import org.openmrs.module.hospitalcore.model.SickOffSimplifier;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.List;

public class SickOffListFragmentController {
	
	public void controller(FragmentModel model) {
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		
		SickOffSimplifier sickOffSimplifier;
		List<SickOffSimplifier> patientSearchList = new ArrayList<SickOffSimplifier>();
		for (SickOff sickOff : hospitalCoreService.getPatientSickOffs(null, null, null)) {
			sickOffSimplifier = new SickOffSimplifier();
			sickOffSimplifier.setSickOffId(sickOff.getSickOffId());
			sickOffSimplifier.setNotes(sickOff.getClinicianNotes());
			sickOffSimplifier.setUser(sickOff.getCreator().getGivenName() + " " + sickOff.getCreator().getFamilyName());
			sickOffSimplifier.setProvider(sickOff.getProvider().getName());
			sickOffSimplifier.setPatientName(sickOff.getPatient().getGivenName() + " "
			        + sickOff.getPatient().getFamilyName());
			sickOffSimplifier.setPatientIdentifier(sickOff.getPatient().getActiveIdentifiers().get(0).getIdentifier());
			sickOffSimplifier.setSickOffStartDate(EhrRegistrationUtils.formatDateAs(sickOff.getSickOffStartDate()));
			sickOffSimplifier.setSickOffEndDate(EhrRegistrationUtils.formatDateAs(sickOff.getSickOffEndDate()));
			sickOffSimplifier.setCreatedOn(EhrRegistrationUtils.formatDateTime(sickOff.getCreatedOn()));
			
			patientSearchList.add(sickOffSimplifier);
		}
		model.addAttribute("sickOffsList", patientSearchList);
	}
}
