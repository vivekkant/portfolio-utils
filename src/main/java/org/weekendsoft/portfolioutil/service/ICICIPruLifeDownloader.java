package org.weekendsoft.portfolioutil.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.Price;
import org.weekendsoft.portfolioutil.util.HTTPDownloader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ICICIPruLifeDownloader implements Downloader {

	private static final Logger LOG = Logger.getLogger(ICICIPruLifeDownloader.class);
	
	private static final String url = "https://buy.iciciprulife.com/buy/funds-all-products.htm";
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static Map<String, Price> allPrices = null;
	

	@Override
	public Map<String, Price> download(List<String> symbols) throws Exception {

    	if (ICICIPruLifeDownloader.allPrices == null) {
    		ICICIPruLifeDownloader.allPrices = getAllPrices();
    	}
    	
    	Map<String, Price> prices = new HashMap<String, Price>();
    	for(String symbol : symbols) {
			
    		Price price = ICICIPruLifeDownloader.allPrices.get(symbol);
			
    		if (price != null) {
				prices.put(symbol, price);
				LOG.debug ("Puttiong Nav for " + price.getSymbol() + " : " + price);
			}
			else {
				LOG.info("Could not download Nav for " + symbol);
			}
		}
    	
    	return prices;
		
	}
    
    private Map<String, Price>  getAllPrices() throws Exception {
    	
    	Map<String, Price> prices = new HashMap<String, Price>();
    	
    	HTTPDownloader downloader = HTTPDownloader.getInstance();
    	String result = downloader.download(url);
    	
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode navJSONArray = (ArrayNode) (mapper.readTree(result));
        
        if (navJSONArray.isArray()) {
        	
        	LOG.debug("Got arrays of JSON string with size : " + navJSONArray.size());
			for (JsonNode node : navJSONArray) {

				Price price = new Price();
				LOG.debug("Parsing API For : " + node.toString());
				
				try {					
					price.setSymbol(			node.get("LAfundCode").asText() + ".ICICIPRU");
					price.setDate(parseDate(	node.get("NAVLatestDate").asText()));
					price.setName(				node.get("Fund").asText());
					price.setPrice(parseFloat(	node.get("NAVLatest").asText()));
									
					LOG.debug("Got quote : " + price);
					prices.put(price.getSymbol(), price);
				} 
				catch (Exception e) {
					LOG.error("Exception while getting NAV : " + node.toString());
				}
			}
        }
        LOG.debug("Successfully parsed the response, total navs received : " + prices.size());
    	
        return prices; 
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
