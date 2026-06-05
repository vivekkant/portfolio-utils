package org.weekendsoft.portfolioutil.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.weekendsoft.portfolioutil.model.Price;

class YahooFinanceQuoteDownloaderTest {

	@Test
	void test() throws Exception {
		Downloader downloader = new YahooFinanceQuoteDownloader();
		
		List<String> request = new ArrayList<String>();
		request.add("DMART.NS");
		request.add("BAJAJFINSV.NS");
		request.add("MON100.NS");
			
		Map<String, Price> quotes = downloader.download(request);
		Assert.assertEquals(request.size(), quotes.size());
	}

}
