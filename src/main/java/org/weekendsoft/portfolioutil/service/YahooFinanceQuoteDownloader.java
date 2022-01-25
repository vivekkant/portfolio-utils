package org.weekendsoft.portfolioutil.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.Price;
import org.weekendsoft.portfolioutil.util.HTTPDownloader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class YahooFinanceQuoteDownloader implements Downloader {
	
	private static final Logger LOG = Logger.getLogger(YahooFinanceQuoteDownloader.class);

	private static final String url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-quotes";
	private static final String key = "ffdc8f3cf7mshdfa87fe1f1e839cp1529d3jsn07400723824d";
	
	@Override
	public Map<String, Price> download(List<String> symbols) throws Exception {

    	LOG.info("Starting downloading for the symbols.." + symbols.toString());

        Map<String, Price> prices = null;
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("region", "IN");
        params.put("lang", "en");
        params.put("symbols", getQuoteString(symbols));
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com");
        headers.put("x-rapidapi-key", key);
        headers.put("useQueryString", "true");
        
    	HTTPDownloader downloader = HTTPDownloader.getInstance();
    	String result = downloader.download(url, headers, params);
        
    	if (result != null && !"".equals(result)) {
            LOG.debug("API response received.. " + result);
            prices = parseResponse(result);
            LOG.info("Retruning total quotes: " + prices.size());
    	}
        
        return prices;
		
	}
    
    private Map<String, Price> parseResponse(String response) throws Exception {
    	
        Map<String, Price> prices = new HashMap<String, Price>();
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(response);
        ArrayNode result = (ArrayNode) (tree.get("quoteResponse").get("result"));
        
        if (result.isArray()) {
        	LOG.debug("Got arrays of JSON string with size : " + result.size());
			for (JsonNode node : result) {

				Price price = new Price();
				LOG.debug("Parsing API For : " + node.toString());

				try {
					price.setSymbol(node.get("symbol").asText());
					price.setName(node.get("shortName").asText());
					price.setPrice(node.get("regularMarketPrice").doubleValue());
					
					LOG.debug("Got quote : " + price);
					prices.put(price.getSymbol(), price);
				} 
				catch (Exception e) {
					LOG.error("Got exception while getting the Quote data : " + node.toString(), e);
				}
			}
        }
        
        return prices;
    }

    private String getQuoteString(List<String> symbols) {
    	StringBuilder quoteString = new StringBuilder();
    	
    	for (int i = 0; i < symbols.size(); i++) {
    		
    		quoteString.append(symbols.get(i));
    		if (i < symbols.size() - 1) {
    			quoteString.append(',');
    		}
    	}
    	
    	return quoteString.toString();
    }

}
