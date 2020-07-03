package org.weekendsoft.portfolioutil.util;

import org.junit.jupiter.api.Test;

class HTTPDownloaderTest {

	@Test
	void test() throws Exception {
		
		HTTPDownloader downloader = HTTPDownloader.getInstance();
		String response = downloader.download("https://www.bankbazaar.com/gold-rate-pune.html");
		System.out.println(response);
	}
	

}
