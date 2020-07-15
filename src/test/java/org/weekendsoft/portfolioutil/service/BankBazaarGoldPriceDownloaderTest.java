package org.weekendsoft.portfolioutil.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Price;

class BankBazaarGoldPriceDownloaderTest {

	private static final String GOLD24K = "24K.BB";

	@Test
	void test() throws Exception {
		Downloader downloader = new BankBazaarGoldPriceDownloader();
		
		List<String> symbols = new ArrayList<String>();
		symbols.add(GOLD24K);
		
		Map<String, Price> prices = downloader.download(symbols);
		
		assertEquals(symbols.size(), prices.size());
		assertTrue(prices.get(GOLD24K).getPrice() > 0);
	}

}
