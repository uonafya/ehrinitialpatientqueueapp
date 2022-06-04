package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.PatientQueueService;
import org.openmrs.module.hospitalcore.model.OpdPatientQueue;
import org.openmrs.module.hospitalcore.model.TriagePatientQueue;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.initialpatientqueueapp.model.ViewQueuedPatients;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.ui.framework.page.PageModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewQueuedPatientsPageController {
	
	public void controller(PageModel model) {
		List<ViewQueuedPatients> viewQueuedPatientsList = new ArrayList<ViewQueuedPatients>();
		Date startDate = DateUtil.getStartOfDay(new Date());
		Date endDate = DateUtil.getEndOfDay(new Date());
		User user = Context.getAuthenticatedUser();
		
		List<OpdPatientQueue> opdPatientQueueList = Context.getService(PatientQueueService.class)
		        .getAllOpdPatientQueueWithinDatePerUser(startDate, endDate, user);
		List<TriagePatientQueue> triagePatientQueueList = Context.getService(PatientQueueService.class)
		        .getAllTriagePatientQueueWithinDatePerUser(startDate, endDate, user);
		ViewQueuedPatients viewQueuedTriagePatients;
		ViewQueuedPatients viewQueuedOpdPatients;
		if (!triagePatientQueueList.isEmpty()) {
			
			for (TriagePatientQueue triagePatientQueue : triagePatientQueueList) {
				viewQueuedTriagePatients = new ViewQueuedPatients();
				viewQueuedTriagePatients.setQueueId(triagePatientQueue.getId());
				viewQueuedTriagePatients.setStatus(triagePatientQueue.getStatus());
				viewQueuedTriagePatients.setPatientNames(triagePatientQueue.getPatientName());
				viewQueuedTriagePatients.setPatientIdentifier(triagePatientQueue.getPatientIdentifier());
				viewQueuedTriagePatients.setVisitDate(EhrRegistrationUtils.formatDate(triagePatientQueue.getCreatedOn()));
				viewQueuedTriagePatients.setVisitStatus(triagePatientQueue.getVisitStatus());
				viewQueuedTriagePatients.setReferralConceptName(triagePatientQueue.getReferralConceptName());
				viewQueuedTriagePatients.setServiceConceptName(triagePatientQueue.getTriageConceptName());
				
				viewQueuedPatientsList.add(viewQueuedTriagePatients);
				
			}
		}
		if (!opdPatientQueueList.isEmpty()) {
			for (OpdPatientQueue opdPatientQueue : opdPatientQueueList) {
				viewQueuedOpdPatients = new ViewQueuedPatients();
				viewQueuedOpdPatients.setQueueId(opdPatientQueue.getId());
				viewQueuedOpdPatients.setStatus(opdPatientQueue.getStatus());
				viewQueuedOpdPatients.setPatientNames(opdPatientQueue.getPatientName());
				viewQueuedOpdPatients.setPatientIdentifier(opdPatientQueue.getPatientIdentifier());
				viewQueuedOpdPatients.setVisitDate(EhrRegistrationUtils.formatDate(opdPatientQueue.getCreatedOn()));
				viewQueuedOpdPatients.setVisitStatus(opdPatientQueue.getVisitStatus());
				viewQueuedOpdPatients.setReferralConceptName(opdPatientQueue.getReferralConceptName());
				viewQueuedOpdPatients.setServiceConceptName(opdPatientQueue.getOpdConceptName());
				
			}
			
		}
		
		model.addAttribute("viewQueuedPatientsList", viewQueuedPatientsList);
	}
}
