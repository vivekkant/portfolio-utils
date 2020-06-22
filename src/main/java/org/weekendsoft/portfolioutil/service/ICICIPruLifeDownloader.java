package org.weekendsoft.portfolioutil.service;

import java.text.SimpleDateFormat;
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

public class ICICIPruLifeDownloader {

	private static final Logger LOG = Logger.getLogger(ICICIPruLifeDownloader.class);
	
	private static final String url = "https://buy.iciciprulife.com/buy/funds-all-products.htm";
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static Map<String, Nav> allNavs = null;
	
    public Map<String, Nav> downloadNavs() throws Exception {
        
    	Map<String, Nav> navs = new HashMap<String, Nav>();
    	LOG.debug("Downloading from URL : " + url);
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        URIBuilder requestURL = new URIBuilder(url);
        LOG.debug("Starting downloading from.." + requestURL.toString());
        
        HttpGet request = new HttpGet(requestURL.build());
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("API response received.. " + result);
            
            LOG.info("Retruning total quotes: " + navs.size());
        }
        

        return navs;
    }
    
    

}
