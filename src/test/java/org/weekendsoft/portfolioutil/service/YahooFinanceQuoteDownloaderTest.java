package org.weekendsoft.portfolioutil.service;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Quote;
import org.weekendsoft.portfolioutil.service.YahooFinanceQuoteDownloader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YahooFinanceQuoteDownloaderTest {

	@Test
	void test() throws Exception {
		YahooFinanceQuoteDownloader downloader = new YahooFinanceQuoteDownloader();
		
		String[] request = {"DMART.NS", "BAJAJFINSV.NS", "UJJIVAN.NS"};
		
		Map<String, Quote> quotes = downloader.downloadQuotes(request);
		assertEquals(request.length, quotes.size());
	}

}
