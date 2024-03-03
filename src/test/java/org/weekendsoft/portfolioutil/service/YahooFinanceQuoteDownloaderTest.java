package org.weekendsoft.portfolioutil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Price;

class YahooFinanceQuoteDownloaderTest {

	@Test
	void test() throws Exception {
		Downloader downloader = new YahooFinanceQuoteDownloader();
		
		List<String> request = new ArrayList<String>();
		request.add("DMART.NS");
		request.add("BAJAJFINSV.NS");
		request.add("N100.BO");
			
		Map<String, Price> quotes = downloader.download(request);
		assertEquals(request.size(), quotes.size());
	}

}
