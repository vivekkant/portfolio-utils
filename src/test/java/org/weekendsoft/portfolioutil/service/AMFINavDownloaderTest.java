package org.weekendsoft.portfolioutil.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
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
		Assert.assertEquals(request.size(), prices.size());
	}

}
