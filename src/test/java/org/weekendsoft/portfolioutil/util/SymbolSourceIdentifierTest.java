package org.weekendsoft.portfolioutil.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.util.SymbolSourceIdentifier.SymbolType;

class SymbolSourceIdentifierTest {

	@Test
	void testForYahoo() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("BAJFINANCE.NS");
		assertEquals(SymbolType.YAHOO, symbolType);
	}
	
	@Test
	void testForAMFI() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("123456");
		assertEquals(SymbolType.AMFI, symbolType);
	}
	
	@Test
	void testForOther() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("GOLD");
		assertEquals(SymbolType.OTHER, symbolType);
	}

	@Test
	void testForOther1() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("BAJFINANCENS");
		assertNotEquals(SymbolType.YAHOO, symbolType);
	}
	
	@Test
	void testForOther2() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("234");
		assertNotEquals(SymbolType.AMFI, symbolType);
	}
	
	@Test
	void testForICICIPru() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("5GS.ICICIPRU");
		assertEquals(SymbolType.ICICIPRU, symbolType);
	}
	
	@Test
	void testForBB() {
		SymbolType symbolType = SymbolSourceIdentifier.identifySymbolSource("GOLD.BBGOLD");
		assertEquals(SymbolType.BBGOLD, symbolType);
	}

}
