package org.weekendsoft.portfolioutil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Quote;

class YahooFinanceQuoteDownloaderTest {

	@Test
	void test() throws Exception {
		YahooFinanceQuoteDownloader downloader = new YahooFinanceQuoteDownloader();
		
		List<String> request = new ArrayList<String>();
		request.add("DMART.NS");
		request.add("BAJAJFINSV.NS");
		request.add("UJJIVAN.NS");
			
		Map<String, Quote> quotes = downloader.downloadQuotes(request);
		assertEquals(request.size(), quotes.size());
	}

}
