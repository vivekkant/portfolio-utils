package org.weekendsoft.portfolioutil.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.Nav;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ICICIPruLifeDownloader {

	private static final Logger LOG = Logger.getLogger(ICICIPruLifeDownloader.class);
	
	private static final String url = "https://buy.iciciprulife.com/buy/funds-all-products.htm";
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static Map<String, Nav> allNavs = null;
	
    private String downloadNavsJSON() throws Exception {
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder requestURL = new URIBuilder(url);
        LOG.debug("Starting downloading from : " + requestURL.toString());
        
        HttpGet request = new HttpGet(requestURL.build());
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        String result = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity);
            LOG.debug("API response received : " + result);
        }
        else {
        	throw new Exception("Null response received from the server");
        }
        
        return result;
    }
    
    public Map<String, Nav>  getAllNavs() throws Exception {
    	
    	if (ICICIPruLifeDownloader.allNavs != null) {
    		return ICICIPruLifeDownloader.allNavs;
    	}
    	
    	Map<String, Nav> navs = new HashMap<String, Nav>();
    	
    	String result = downloadNavsJSON();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode navJSONArray = (ArrayNode) (mapper.readTree(result));
        
        if (navJSONArray.isArray()) {
        	
        	LOG.debug("Got arrays of JSON string with size : " + navJSONArray.size());
			for (JsonNode node : navJSONArray) {
				Nav nav = new Nav();
				LOG.debug("Parsing API For : " + node.toString());
				
				nav.setCode(			node.get("LAfundCode").asText());
				nav.setDate(parseDate(	node.get("NAVLatestDate").asText()));
				nav.setIsin(			node.get("SFIN").asText());
				nav.setName(			node.get("Fund").asText());
				nav.setNav(parseFloat(	node.get("NAVLatest").asText()));
								
				LOG.debug("Got quote : " + nav);
				navs.put(nav.getCode(), nav);
			}
        }
        LOG.debug("Successfully parsed the response, total navs received : " + navs.size());
    	
        ICICIPruLifeDownloader.allNavs = navs;
        return navs; 
    }
    
    public Map<String, Nav> getNavForCodes(String[] codes) throws Exception {
    	
    	Map<String, Nav> allNavs 	= getAllNavs();
    	Map<String, Nav> navs 		= new HashMap<String, Nav>();
    	
    	for (String code : codes) {
    		
    		Nav nav = allNavs.get(code);
    		if (nav != null) {
    			navs.put(code, nav);
    			LOG.debug("Got Nav for : " + nav);
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
