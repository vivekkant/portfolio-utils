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
import org.weekendsoft.portfolioutil.model.Price;

public class AMFINavDownloader implements Downloader {

	private static final Logger LOG = Logger.getLogger(AMFINavDownloader.class);
	
	private static final String url = "https://www.amfiindia.com/spages/NAVAll.txt";
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static Map<String, Price> allPrices = null;
	
	@Override
	public Map<String, Price> download(List<String> symbols) throws Exception {

    	if (AMFINavDownloader.allPrices == null) {
    		AMFINavDownloader.allPrices = downloadPrices();
    	}
    	
    	Map<String, Price> prices = new HashMap<String, Price>();
    	for(String symbol : symbols) {
			
    		Price price = AMFINavDownloader.allPrices.get(symbol);
			
    		if (price != null) {
    			prices.put(symbol, price);
				LOG.debug ("Puttiong Price for " + price.getSymbol() + " : " + price);
			}
			else {
				LOG.info("Could not download Nav for " + symbol);
			}
		}
    	
    	return prices;
    	
	}

	
    public Map<String, Price> downloadPrices() throws Exception {
        
    	URL website = new URL(url);
        LOG.debug("Downloading from URL : " + url);
        
        URLConnection connection = website.openConnection();
        Map<String, Price> response = new HashMap<String, Price>();
        
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
        	if (filter(inputLine)) {
        		Price price = parsePrice(inputLine);
        		if (price != null) {
        			response.put(price.getSymbol(), price);
        		}
        	}
        }
        LOG.debug("Downloaded total NAVs : " + response.size());
            
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
				//Ignore
			}
    	}
    			
    	return toInclude;		
    }
    
    private Price parsePrice(String line) {
    	
    	Price price = null;
    	String[] parts = line.split(";");
    	
    	if (parts.length >= 6) {
    		
    		price = new Price();
    		try {
				price.setSymbol(parts[0].trim());
			} catch (NumberFormatException e) {
				return null;
			}
    		
    		price.setName(parts[3].trim());
    		
    		try {
				price.setPrice(Double.parseDouble(parts[4].trim()));
			} catch (NumberFormatException e) {
				return null;
			}
    		
    		try {
				price.setDate(formatter.parse(parts[5]));
			} catch (ParseException e) {
				//Igore
			}
    		
    	}
    	
    	return price;
    }

}
