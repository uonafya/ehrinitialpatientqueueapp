package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.model.AppointmentServiceDefinition;
import org.openmrs.module.appointments.model.AppointmentServiceType;
import org.openmrs.module.appointments.service.AppointmentServiceDefinitionService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.List;

public class ManageAppointmentsServiceTypesFragmentController {
	
	public void controller(FragmentModel model) {
        AppointmentServiceDefinitionService appointmentServiceDefinitionService = Context
                .getService(AppointmentServiceDefinitionService.class);

        HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);

        List<AppointmentServiceDefinition> appointmentServiceDefinitionList = new ArrayList<>(appointmentServiceDefinitionService.getAllAppointmentServices(false));
        List<AppointmentServiceType> appointmentServiceTypeList = new ArrayList<>(hospitalCoreService.getAppointmentServiceType());

        model.addAttribute("appointmentServiceDefinitionList", appointmentServiceDefinitionList);
        model.addAttribute("appointmentServicesTypes", appointmentServiceTypeList);
    }
}
