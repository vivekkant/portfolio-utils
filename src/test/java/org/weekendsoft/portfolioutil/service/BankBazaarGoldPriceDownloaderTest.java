package org.weekendsoft.portfolioutil.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BankBazaarGoldPriceDownloaderTest {


	@Test
	void test() {
		BankBazaarGoldPriceDownloader downloader = new BankBazaarGoldPriceDownloader();
		double price = downloader.download24KGoldPrice();
		System.out.println("Price downloaded : " + price);
		assertTrue(price > 0);
	}

}
