package org.openmrs.module.initialpatientqueueapp.fragment.controller;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReferralInfoFragmentController {
	
	public void controller(FragmentModel model) {
		
		// create list of counties
		List<String> countyList = new ArrayList<String>();
		List<Location> locationList = Context.getLocationService().getAllLocations();
		for (Location loc : locationList) {
			String locationCounty = loc.getCountyDistrict();
			if (!StringUtils.isEmpty(locationCounty) && !StringUtils.isBlank(locationCounty)) {
				countyList.add(locationCounty);
			}
		}
		Set<String> uniqueCountyList = new HashSet<String>(countyList);
		
		KenyaEmrService kenyaEmrService = Context.getService(KenyaEmrService.class);
		
		model.addAttribute("defaultLocation", kenyaEmrService.getDefaultLocation());
		model.addAttribute("countyList", uniqueCountyList);
	}
	
}
