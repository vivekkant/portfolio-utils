package org.weekendsoft.portfolioutil.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.weekendsoft.portfolioutil.model.Price;

class ICICIPruLifeDownloaderTest {



	@Test
	void testSomeCode() throws Exception {
		
		
		List<String> codes = new ArrayList<String>();
		codes.add("5GTH.ICICIPRU");
		codes.add("2RIC.ICICIPRU");
		
		Downloader downloader = new ICICIPruLifeDownloader();
		Map<String, Price> prices = downloader.download(codes);
		Assert.assertEquals(codes.size(), prices.size());
	}

}
