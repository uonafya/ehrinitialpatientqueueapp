package org.openmrs.module.initialpatientqueueapp.metadata;

import org.openmrs.module.initialpatientqueueapp.InitialPatientQueueConstants;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.metadatadeploy.bundle.Requires;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.encounterType;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.form;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.idSet;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.privilege;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.role;

/**
 * Implementation of access control to the app.
 */
@Component
@Requires(org.openmrs.module.kenyaemr.metadata.SecurityMetadata.class)
public class InitialPatientQueueMetadata extends AbstractMetadataBundle {
	
	public static class _Privilege {
		
		public static final String APP_PQ_MODULE_APP = "App: initialpatientqueueapp.queue";
		
		public static final String APP_EHR_APPOINTMENT = "App: initialpatientqueueapp.appointment";
	}
	
	public static final class _Role {
		
		public static final String APPLICATION_PQ_MODULE = "EHR Patient Queue application";
		
		public static final String EHR_APPOINTMENT_MODULE = "EHR Appointment application";
	}
	
	//encounter type here
	public static class _EncounterTypes {
		
		public static final String PATIENT_QUEUE_INITIAL = "356d447a-b494-11ea-8337-f7bcaf3e8fec";
	}
	
	//form here
	public static class _Form {
		
		public static final String PATIENT_QUEUE_INITIAL = "f025e43a-b470-11ea-8a58-e7fc06ee7901";
	}
	
	/**
	 * @see AbstractMetadataBundle#install()
	 */
	@Override
	public void install() {
		
		install(privilege(_Privilege.APP_PQ_MODULE_APP, "Able to access Key patient Queue module features for EHR "));
		install(role(_Role.APPLICATION_PQ_MODULE, "Can access Key patient queue module App for EHR",
		    idSet(org.openmrs.module.kenyaemr.metadata.SecurityMetadata._Role.API_PRIVILEGES_VIEW_AND_EDIT),
		    idSet(_Privilege.APP_PQ_MODULE_APP)));
		install(privilege(_Privilege.APP_EHR_APPOINTMENT, "Able to access Key patient appointments module features for EHR "));
		install(role(_Role.EHR_APPOINTMENT_MODULE, "Can access Key patient appointment module App for EHR",
		    idSet(org.openmrs.module.kenyaemr.metadata.SecurityMetadata._Role.API_PRIVILEGES_VIEW_AND_EDIT),
		    idSet(_Privilege.APP_EHR_APPOINTMENT)));
		
		//install the encounter types
		install(encounterType("Patient queue initial", "Initial Patient Queuing", _EncounterTypes.PATIENT_QUEUE_INITIAL));
		//install the forms
		install(form("Patient queue initial", "The first form that is filled when queueing patients",
		    _EncounterTypes.PATIENT_QUEUE_INITIAL, "1", _Form.PATIENT_QUEUE_INITIAL));
	}
}
