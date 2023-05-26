package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.PatientQueueService;
import org.openmrs.module.hospitalcore.model.OpdPatientQueue;
import org.openmrs.module.hospitalcore.model.TriagePatientQueue;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.initialpatientqueueapp.EhrRegistrationUtils;
import org.openmrs.module.initialpatientqueueapp.model.ProviderSimplifier;
import org.openmrs.module.initialpatientqueueapp.model.ViewQueuedPatients;
import org.openmrs.module.initialpatientqueueapp.web.controller.utils.RegistrationWebUtils;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewQueuedPatientsFragmentController {
	
	DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
	
	public void controller(FragmentModel model) {
		List<ViewQueuedPatients> viewQueuedPatientsList = new ArrayList<ViewQueuedPatients>();
		Date startDate = DateUtil.getStartOfDay(new Date());
		Date endDate = DateUtil.getEndOfDay(new Date());
		User user = Context.getAuthenticatedUser();
		
		List<OpdPatientQueue> opdPatientQueueList = Context.getService(PatientQueueService.class)
		        .getAllOpdPatientQueueWithinDatePerUser(startDate, endDate, user, null);
		List<TriagePatientQueue> triagePatientQueueList = Context.getService(PatientQueueService.class)
		        .getAllTriagePatientQueueWithinDatePerUser(startDate, endDate, user, null);
		ViewQueuedPatients viewQueuedTriagePatients;
		ViewQueuedPatients viewQueuedOpdPatients;
		if (!triagePatientQueueList.isEmpty()) {
			
			for (TriagePatientQueue triagePatientQueue : triagePatientQueueList) {
				viewQueuedTriagePatients = new ViewQueuedPatients();
				viewQueuedTriagePatients.setQueueId(triagePatientQueue.getId());
				viewQueuedTriagePatients.setSex(triagePatientQueue.getSex());
				viewQueuedTriagePatients.setStatus(triagePatientQueue.getStatus());
				viewQueuedTriagePatients.setPatientNames(triagePatientQueue.getPatientName());
				viewQueuedTriagePatients.setPatientIdentifier(triagePatientQueue.getPatientIdentifier());
				viewQueuedTriagePatients.setVisitDate(EhrRegistrationUtils.formatDate(triagePatientQueue.getCreatedOn()));
				viewQueuedTriagePatients.setVisitStatus(triagePatientQueue.getVisitStatus());
				viewQueuedTriagePatients.setServiceConceptName(triagePatientQueue.getTriageConceptName());
				viewQueuedTriagePatients.setCategory(triagePatientQueue.getCategory());
				viewQueuedTriagePatients.setStartTime(formatter.format(triagePatientQueue.getCreatedOn()));
				viewQueuedTriagePatients.setDuration(EhrRegistrationUtils.unitsSince(new Date(),
				    triagePatientQueue.getCreatedOn()));
				viewQueuedTriagePatients.setProvider(HospitalCoreUtils.getProviderNames(viewQueuedTriagePatients
				        .getProvider()));
				
				viewQueuedPatientsList.add(viewQueuedTriagePatients);
				
			}
		}
		if (!opdPatientQueueList.isEmpty()) {
			for (OpdPatientQueue opdPatientQueue : opdPatientQueueList) {
				viewQueuedOpdPatients = new ViewQueuedPatients();
				viewQueuedOpdPatients.setQueueId(opdPatientQueue.getId());
				viewQueuedOpdPatients.setSex(opdPatientQueue.getSex());
				viewQueuedOpdPatients.setStatus(opdPatientQueue.getStatus());
				viewQueuedOpdPatients.setPatientNames(opdPatientQueue.getPatientName());
				viewQueuedOpdPatients.setPatientIdentifier(opdPatientQueue.getPatientIdentifier());
				viewQueuedOpdPatients.setVisitDate(EhrRegistrationUtils.formatDate(opdPatientQueue.getCreatedOn()));
				viewQueuedOpdPatients.setVisitStatus(opdPatientQueue.getVisitStatus());
				viewQueuedOpdPatients.setServiceConceptName(opdPatientQueue.getOpdConceptName());
				viewQueuedOpdPatients.setCategory(opdPatientQueue.getCategory());
				viewQueuedOpdPatients.setStartTime(formatter.format(opdPatientQueue.getCreatedOn()));
				viewQueuedOpdPatients
				        .setDuration(EhrRegistrationUtils.unitsSince(new Date(), opdPatientQueue.getCreatedOn()));
				viewQueuedOpdPatients.setProvider(HospitalCoreUtils.getProviderNames(opdPatientQueue.getProvider()));
				
				viewQueuedPatientsList.add(viewQueuedOpdPatients);
				
			}
			
		}
		
		model.addAttribute("viewQueuedPatientsList", viewQueuedPatientsList);
		
		List<Provider> allProvidersList = Context.getProviderService().getAllProviders();
		List<ProviderSimplifier> simplifiedProviderList = new ArrayList<ProviderSimplifier>();
		ProviderSimplifier providerSimplifier;
		
		for (Provider provider : allProvidersList) {
			providerSimplifier = new ProviderSimplifier();
			providerSimplifier.setProviderId(provider.getProviderId());
			providerSimplifier.setIdentifier(provider.getIdentifier());
			providerSimplifier.setPersonId(provider.getPerson().getPersonId());
			if (!(Context.getUserService().getUsersByPerson(provider.getPerson(), false).isEmpty())) {
				providerSimplifier.setUserId(Context.getUserService().getUsersByPerson(provider.getPerson(), false).get(0)
				        .getUserId());
			}
			providerSimplifier.setNames(provider.getIdentifier() + "-" + provider.getPerson().getGivenName() + " "
			        + provider.getPerson().getFamilyName());
			simplifiedProviderList.add(providerSimplifier);
		}
		
		model.addAttribute("listProviders", simplifiedProviderList);
	}
	
	public void updatePatientQueue(@RequestParam(value = "queueId", required = false) String queueId,
	        @RequestParam(value = "servicePoint", required = false) String servicePoint,
	        @RequestParam(value = "rooms2", required = false) String rooms2,
	        @RequestParam(value = "rooms1", required = false) String rooms1,
	        @RequestParam(value = "providerToVisit", required = false) Provider providerToVisit) {
		String[] servicePointPart = servicePoint.split("\\s+");
		Concept concept = null;
		if (StringUtils.isNotBlank(rooms2)) {
			concept = Context.getConceptService().getConcept(rooms2);
		}
		if (StringUtils.isNotBlank(queueId) && StringUtils.isNotBlank(servicePoint) && concept != null) {
			if (servicePointPart[1].equals("Triage")) {
				//update the triage queue
				TriagePatientQueue queueTriage = Context.getService(PatientQueueService.class).getTriagePatientQueueById(
				    Integer.valueOf(queueId));
				queueTriage.setTriageConcept(concept);
				queueTriage.setTriageConceptName(concept.getDisplayString());
				if (providerToVisit != null) {
					queueTriage.setProvider(providerToVisit.getIdentifier());
				}
				Context.getService(PatientQueueService.class).saveTriagePatientQueue(queueTriage);
			} else {
				//update the opd queue
				OpdPatientQueue queueOpd = Context.getService(PatientQueueService.class).getOpdPatientQueueById(
				    Integer.valueOf(queueId));
				queueOpd.setOpdConcept(concept);
				queueOpd.setOpdConceptName(concept.getDisplayString());
				if (providerToVisit != null) {
					queueOpd.setProvider(providerToVisit.getIdentifier());
				}
				Context.getService(PatientQueueService.class).saveOpdPatientQueue(queueOpd);
			}
		}
		
	}
}
