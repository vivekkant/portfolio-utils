package org.weekendsoft.portfolioutil.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Nav;

import static org.junit.jupiter.api.Assertions.*;

class AMFINavDownloaderTest {

	@Test
	void test() throws Exception {
		AMFINavDownloader downloader = new AMFINavDownloader();
		
		List<Integer> request = new ArrayList<Integer>();
		request.add(108466);
		request.add(119707);
		request.add(122639);
		
		Map<Integer, Nav> navs = downloader.downloadNavs(request);
		assertEquals(request.size(), navs.size());
	}

}
