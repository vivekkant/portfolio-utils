package org.weekendsoft.portfolioutil.service;

import java.util.Arrays;
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
import org.weekendsoft.portfolioutil.model.Quote;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class YahooFinanceQuoteDownloader {
	
	private static final Logger LOG = Logger.getLogger(YahooFinanceQuoteDownloader.class);

	private static final String url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-quotes?region=IN&lang=en&symbols=DMART.NS";
	private static final String key = "ffdc8f3cf7mshdfa87fe1f1e839cp1529d3jsn07400723824d";
	
    public Map<String, Quote> downloadQuotes(String[] symbols) throws Exception {
    	
    	LOG.info("Starting downloading for the symbols.." + Arrays.toString(symbols));

        Map<String, Quote> quotes = null;
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        URIBuilder requestURL = new URIBuilder(url);
        requestURL.setParameter("region", "IN")
        		  .setParameter("lang", "en")
        		  .setParameter("symbols", getQuoteString(symbols));
        LOG.debug("Starting downloading from.." + requestURL.toString());
        
        HttpGet request = new HttpGet(requestURL.build());
        
        request.addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com");
        request.addHeader("x-rapidapi-key", key);
        request.addHeader("useQueryString", "true");
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("API response received.. " + result);
            
            quotes = parseResponse(result);
            LOG.info("Retruning total quotes: " + quotes.size());
        }
        
        return quotes;
    }
    
    private Map<String, Quote> parseResponse(String response) throws Exception {
    	
        Map<String, Quote> quotes = new HashMap<String, Quote>();
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(response);
        ArrayNode result = (ArrayNode) (tree.get("quoteResponse").get("result"));
        
        if (result.isArray()) {
        	LOG.debug("Got arrays of JSON string with size :" + result.size());
			for (JsonNode node : result) {
				Quote quote = new Quote();
				
				quote.setSymbol(node.get("symbol").asText());
				quote.setLongName(node.get("longName").asText());
				quote.setRegularMarketPrice(node.get("regularMarketPrice").floatValue());
				quote.setRegularMarketPreviousClose(node.get("regularMarketPreviousClose").floatValue());
				
				LOG.debug("Got quote : " + quote);
				quotes.put(quote.getSymbol(), quote);
			}
        }
        
        return quotes;
    }

    private String getQuoteString(String[] symbols) {
    	StringBuilder quoteString = new StringBuilder();
    	
    	for (int i = 0; i < symbols.length; i++) {
    		quoteString.append(symbols[i]);
    		if (i < symbols.length - 1) {
    			quoteString.append(',');
    		}
    	}
    	
    	return quoteString.toString();
    }

}
