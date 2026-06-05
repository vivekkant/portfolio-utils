package org.weekendsoft.portfolioutil.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.weekendsoft.portfolioutil.model.Price;

class BankBazaarGoldPriceDownloaderTest {

	private static final String GOLD24K = "24K.BB";

	@Test
	void testBBGold() throws Exception {
		Downloader downloader = new BankBazaarGoldPriceDownloader();
		
		List<String> symbols = new ArrayList<String>();
		symbols.add(GOLD24K);
		
		Map<String, Price> prices = downloader.download(symbols);
		
		Assert.assertEquals(symbols.size(), prices.size());
		Assert.assertTrue(prices.get(GOLD24K).getPrice() > 0);
	}

}
