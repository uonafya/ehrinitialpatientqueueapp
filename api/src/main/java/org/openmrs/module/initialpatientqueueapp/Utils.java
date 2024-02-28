package org.openmrs.module.initialpatientqueueapp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {
	
	public static String getDashBoards(int dashBoardId, int patientId) {
		
		String METABASE_SITE_URL = "https://analytics.ilarahealth.com";
		String METABASE_SECRET_KEY = "a72e1cf49e68111b9b275fc337d75b7b9c4bec92f034e9c586b6f709cb7f1ba5";
		String iframeUrl = "";
		
		// Set the expiration time (10 minutes from now)
		long expirationTimeMillis = System.currentTimeMillis() + (10 * 60 * 1000);
		Date expirationDate = new Date(expirationTimeMillis);
		
		try {
			// Build the token payload
			Map<String, Object> payload = new HashMap<String, Object>();
			Map<String, Object> resource = new HashMap<String, Object>();
			resource.put("dashboard", dashBoardId);
			payload.put("resource", resource);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("patient_id", new Integer[] { patientId });
			payload.put("params", params);
			payload.put("exp", expirationDate.getTime() / 1000);
			
			// Generate the JWT token
			Algorithm algorithm = Algorithm.HMAC256(METABASE_SECRET_KEY);
			String token = JWT.create().withPayload(payload).sign(algorithm);
			
			// Build the iframe URL
			iframeUrl = METABASE_SITE_URL + "/embed/dashboard/" + token + "#bordered=true&titled=true";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return iframeUrl;
		
	}
}
