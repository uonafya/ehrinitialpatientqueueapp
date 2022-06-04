package org.openmrs.module.initialpatientqueueapp.page.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.PatientQueueService;
import org.openmrs.module.hospitalcore.model.OpdPatientQueue;
import org.openmrs.module.initialpatientqueueapp.model.ViewQueuedPatients;
import org.openmrs.ui.framework.page.PageModel;

import java.util.ArrayList;
import java.util.List;

public class ViewQueuedPatientsPageController {

  public void controller(PageModel model) {
    List<ViewQueuedPatients> viewQueuedPatientsList = new ArrayList<ViewQueuedPatients>();

    List<OpdPatientQueue> triagePatientQueueList = Context.getService(PatientQueueService.class).getAllPatientInQueue();

    model.addAttribute("viewQueuedPatientsList", viewQueuedPatientsList);
  }
}
