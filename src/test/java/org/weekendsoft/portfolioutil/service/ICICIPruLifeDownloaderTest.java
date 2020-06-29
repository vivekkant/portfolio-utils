package org.weekendsoft.portfolioutil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Nav;

class ICICIPruLifeDownloaderTest {



	@Test
	void testSomeCode() throws Exception {
		
		
		List<String> codes = new ArrayList<String>();
		codes.add("5GTH.ICICIPRU");
		codes.add("2RIC.ICICIPRU");
		
		ICICIPruLifeDownloader downloader = new ICICIPruLifeDownloader();
		Map<String, Nav> navs = downloader.downloadNavs(codes);
		assertEquals(codes.size(), navs.size());
	}

}
