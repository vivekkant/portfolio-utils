package org.weekendsoft.portfolioutil.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.Nav;

class ICICIPruLifeDownloaderTest {



	@Test
	void testSomeCode() throws Exception {
		
		String[] codes = {"5GTH", "2RIC"};
		
		ICICIPruLifeDownloader downloader = new ICICIPruLifeDownloader();
		Map<String, Nav> navs = downloader.getNavForCodes(codes);
		assertEquals(codes.length, navs.size());
	}

}
