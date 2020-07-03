package org.weekendsoft.portfolioutil.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.Nav;
import org.weekendsoft.portfolioutil.util.HTTPDownloader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ICICIPruLifeDownloader {

	private static final Logger LOG = Logger.getLogger(ICICIPruLifeDownloader.class);
	
	private static final String url = "https://buy.iciciprulife.com/buy/funds-all-products.htm";
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static Map<String, Nav> allNavs = null;
    
    public Map<String, Nav>  getAllNavs() throws Exception {
    	
    	Map<String, Nav> navs = new HashMap<String, Nav>();
    	
    	HTTPDownloader downloader = HTTPDownloader.getInstance();
    	String result = downloader.download(url);
    	
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode navJSONArray = (ArrayNode) (mapper.readTree(result));
        
        if (navJSONArray.isArray()) {
        	
        	LOG.debug("Got arrays of JSON string with size : " + navJSONArray.size());
			for (JsonNode node : navJSONArray) {

				Nav nav = new Nav();
				LOG.debug("Parsing API For : " + node.toString());
				
				try {					
					nav.setCode(			node.get("LAfundCode").asText() + ".ICICIPRU");
					nav.setDate(parseDate(	node.get("NAVLatestDate").asText()));
					nav.setIsin(			node.get("SFIN").asText());
					nav.setName(			node.get("Fund").asText());
					nav.setNav(parseFloat(	node.get("NAVLatest").asText()));
									
					LOG.debug("Got quote : " + nav);
					navs.put(nav.getCode(), nav);
				} 
				catch (Exception e) {
					LOG.error("Exception while getting NAV : " + node.toString());
				}
			}
        }
        LOG.debug("Successfully parsed the response, total navs received : " + navs.size());
    	
        return navs; 
    }
    
    
    public Map<String, Nav> downloadNavs(List<String> codes) throws Exception {
		
    	if (ICICIPruLifeDownloader.allNavs == null) {
    		ICICIPruLifeDownloader.allNavs = getAllNavs();
    	}
    	
    	Map<String, Nav> navs = new HashMap<String, Nav>();
    	for(String code : codes) {
			
    		Nav nav = ICICIPruLifeDownloader.allNavs.get(code);
			
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
    
    private Date parseDate(String str) {
    	
    	Date date = null;
    	
    	try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			//Ignore
		}
    	
    	return date;
    }
    
    private float parseFloat(String str) {
    	
    	float f = 0.0f;
    	
    	try {
			f = Float.parseFloat(str);
		} catch (NumberFormatException e) {
			//Ignore
		}
    	
    	return f;
    }

}
