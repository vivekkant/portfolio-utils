package org.weekendsoft.portfolioutil.util;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.weekendsoft.portfolioutil.util.SymbolSourceIdentifier.SymbolType;

class SymbolSourceIdentifierTest {

	@Test
	void testForYahoo() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("BAJFINANCE.NS");
		Assert.assertEquals(SymbolType.YAHOO, symbolType);
	}
	
	@Test
	void testForAMFI() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("123456");
		Assert.assertEquals(SymbolType.AMFI, symbolType);
	}
	
	@Test
	void testForOther() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("GOLD");
		Assert.assertEquals(SymbolType.OTHER, symbolType);
	}

	@Test
	void testForOther1() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("BAJFINANCENS");
		Assert.assertNotEquals(SymbolType.YAHOO, symbolType);
	}
	
	@Test
	void testForOther2() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("234");
		Assert.assertNotEquals(SymbolType.AMFI, symbolType);
	}
	
	@Test
	void testForICICIPru() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("5GS.ICICIPRU");
		Assert.assertEquals(SymbolType.ICICIPRU, symbolType);
	}
	
	@Test
	void testForBB() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("GOLD.BBGOLD");
		Assert.assertEquals(SymbolType.BBGOLD, symbolType);
	}

}
