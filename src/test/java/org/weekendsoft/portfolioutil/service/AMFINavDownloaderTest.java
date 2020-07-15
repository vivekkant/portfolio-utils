package org.weekendsoft.portfolioutil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Price;

class AMFINavDownloaderTest {

	@Test
	void test() throws Exception {
		Downloader downloader = new AMFINavDownloader();
		
		List<String> request = new ArrayList<String>();
		request.add("108466");
		request.add("119707");
		request.add("122639");
		
		Map<String, Price> prices = downloader.download(request);
		assertEquals(request.size(), prices.size());
	}

}
