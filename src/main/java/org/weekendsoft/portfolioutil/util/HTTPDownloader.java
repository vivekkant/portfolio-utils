package org.weekendsoft.portfolioutil.util;

import java.util.Arrays;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HTTPDownloader {
	
	private static final Logger LOG = Logger.getLogger(HTTPDownloader.class);
	
	private static HTTPDownloader instance = null;
	
	public static HTTPDownloader getInstance() {
		
		if (instance == null) {
			instance = new HTTPDownloader();
		}
		
		return instance;
	}
	
	public String download(String url) throws Exception {
		
       return download(url, null, null);
	}
	
	public String download(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
		
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        URIBuilder requestURL = new URIBuilder(url);
        if (params != null) {
        	for (String key : params.keySet()) {
        		requestURL.setParameter(key, params.get(key));
        	}
        }
        LOG.debug("Starting downloading from : " + requestURL.toString());
        
        HttpGet request = new HttpGet(requestURL.build());
        
        if (headers != null) {
        	for (String key : headers.keySet()) {
        		request.addHeader(key, headers.get(key));
        		LOG.debug("Addeding header : " + key + " : " + headers.get(key));
        	}
        }
        
        LOG.debug("Executing the request....");
        CloseableHttpResponse response = httpClient.execute(request);
        
        LOG.debug("Response received : " + Arrays.toString(response.getAllHeaders()));
        
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

}
