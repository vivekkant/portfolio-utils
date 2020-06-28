package org.weekendsoft.portfolioutil.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.Nav;

public class AMFINavDownloader {

	private static final Logger LOG = Logger.getLogger(AMFINavDownloader.class);
	
	private static final String url = "https://www.amfiindia.com/spages/NAVAll.txt";
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static Map<String, Nav> allNavs = null;
	
    public Map<String, Nav> downloadNavs() throws Exception {
        
    	URL website = new URL(url);
        LOG.debug("Downloading from URL : " + url);
        
        URLConnection connection = website.openConnection();
        Map<String, Nav> response = new HashMap<String, Nav>();
        
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
        LOG.debug("Downloaded total NAVs : " + response.size());
            
        in.close();

        return response;
    }
    
    public Map<String, Nav> downloadNavs(List<String> codes) throws Exception {
		
    	if (AMFINavDownloader.allNavs == null) {
    		AMFINavDownloader.allNavs = downloadNavs();
    	}
    	
    	Map<String, Nav> navs = new HashMap<String, Nav>();
    	for(String code : codes) {
			
    		Nav nav = AMFINavDownloader.allNavs.get(code);
			
    		if (nav != null) {
				navs.put(code, nav);
				LOG.debug ("Puttiong Nav for " + nav.getCode() + " : " + nav);
			}
			else {
				LOG.info("Could not download Nav for " + code);
			}
		}
    	
    	return navs;
    }
    
    private boolean filter(String line) {
    	
    	boolean toInclude = false;
    	
    	if (line != null && line.trim().length() > 6) {
    		String code = line.substring(0, 6);
    		try {
				Integer.parseInt(code);
				toInclude = true;
			} catch (NumberFormatException e) {
				//Ignore
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
				nav.setCode(parts[0].trim());
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
				//Igore
			}
    		
    	}
    	
    	return nav;
    }

}
