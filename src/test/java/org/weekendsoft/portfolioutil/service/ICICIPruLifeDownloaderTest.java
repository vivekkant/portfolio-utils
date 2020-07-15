package org.weekendsoft.portfolioutil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Price;

class ICICIPruLifeDownloaderTest {



	@Test
	void testSomeCode() throws Exception {
		
		
		List<String> codes = new ArrayList<String>();
		codes.add("5GTH.ICICIPRU");
		codes.add("2RIC.ICICIPRU");
		
		Downloader downloader = new ICICIPruLifeDownloader();
		Map<String, Price> prices = downloader.download(codes);
		assertEquals(codes.size(), prices.size());
	}

}
