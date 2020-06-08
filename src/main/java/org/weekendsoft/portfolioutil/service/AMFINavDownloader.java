package org.weekendsoft.smallhacks.mfnav;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class AMFINavDownloader {

	private static final String url = "https://www.amfiindia.com/spages/NAVAll.txt";
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	
    public Map<Integer, Nav> downloadNavs() throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        Map<Integer, Nav> response = new HashMap<Integer, Nav>();
        
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
        	if (filter(inputLine)) {
        		Nav nav = parseNav(inputLine);
        		if (nav != null) {
        			response.put(nav.getCode(), nav);
        		}
        	}
        }
            
        in.close();

        return response;
    }
    
    private boolean filter(String line) {
    	boolean toInclude = false;
    	
    	if (line != null && line.trim().length() > 6) {
    		String code = line.substring(0, 6);
    		try {
				Integer.parseInt(code);
				toInclude = true;
			} catch (NumberFormatException e) {
			}
    	}
    			
    	return toInclude;		
    }
    
    private Nav parseNav(String line) {
    	
    	Nav nav = null;
    	String[] parts = line.split(";");
    	
    	if (parts.length >= 6) {
    		
    		nav = new Nav();
    		try {
				nav.setCode(Integer.parseInt(parts[0].trim()));
			} catch (NumberFormatException e) {
				return null;
			}
    		
    		nav.setIsin(parts[1].trim());
    		nav.setIsin2(parts[2].trim());
    		nav.setName(parts[3].trim());
    		
    		try {
				nav.setNav(Float.parseFloat(parts[4].trim()));
			} catch (NumberFormatException e) {
				return null;
			}
    		
    		try {
				nav.setDate(formatter.parse(parts[5]));
			} catch (ParseException e) {
			}
    		
    	}
    	
    	return nav;
    }

}
