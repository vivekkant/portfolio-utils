package org.weekendsoft.portfolioutil.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.Price;
import org.weekendsoft.portfolioutil.util.HTTPDownloader;

public class BankBazaarGoldPriceDownloader implements Downloader {

	private static final Logger LOG = Logger.getLogger(BankBazaarGoldPriceDownloader.class);
	
	private static final String url = "https://www.5paisa.com/commodity-trading/gold-rate-today";
	
//	private static final String GOLD24K = "24K.BB";
	private static final String GOLD24K_NAME = "24K Gold";
	
	
	@Override
	public Map<String, Price> download(List<String> symbols) throws Exception {
		
		Map<String, Price> prices = new HashMap<String, Price>();
		
		for (String symbol : symbols) {
//			if (symbols.contains(GOLD24K)) {
				
				Price price = new Price();
				price.setSymbol(symbol);
				price.setPrice(download24KGoldPrice());
				price.setName(GOLD24K_NAME);
				
				prices.put(symbol, price);
//			}
		}
		
		return prices;
	}

	
	private double download24KGoldPrice() {
		double price = 5000;
		
		try {
			HTTPDownloader downloader = HTTPDownloader.getInstance();
			String response = downloader.download(url);
			price = Double.parseDouble(parse(response));
			price = price / 10;
			LOG.info("Gold Price downloaded : " + price);
		} 
		catch (Exception e) {
			LOG.error("Unable to download gold price", e);
		}
		
		return price;
	}
	
	public String parse(String response) throws Exception {
		String anchorline = "<span>24K Gold</span>";
		String parseStartTag = "<div class=\"gold__value\">";
		String parseEndTag = "</strong>";
		
		
		int anchorPos = response.indexOf(anchorline);
		if (anchorPos != -1) {
			
			int parseStartPos = response.indexOf(parseStartTag, anchorPos + anchorline.length());
			int parseEndPos = response.indexOf(parseEndTag, parseStartPos + parseStartTag.length());
			if (parseStartPos != -1 && parseEndPos != -1) {
				String priceStr = response.substring(parseStartPos, parseEndPos);
				priceStr = priceStr.substring(priceStr.indexOf("<strong>") + 9);
				return priceStr;
			}
			else 
			{
				throw new Exception("Unable to parse tags");
			}
		}
		else {
			throw new Exception("Unable to find the anchor line tag");
		}
	}


}
