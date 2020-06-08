package org.weekendsoft.portfolioutil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Nav;

class AMFINavDownloaderTest {

	@Test
	void test() throws Exception {
		AMFINavDownloader downloader = new AMFINavDownloader();
		
		int[] request = {120792,119707,122639};
		
		Map<Integer, Nav> navs = downloader.downloadNavs(request);
		assertEquals(request.length, navs.size());
	}

}
