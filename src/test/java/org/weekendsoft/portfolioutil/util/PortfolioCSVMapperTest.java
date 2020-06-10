package org.weekendsoft.portfolioutil.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;

class PortfolioCSVMapperTest {
	
	static File csvFile = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File tempdir = new File(System.getProperty("java.io.tmpdir"));
		csvFile = new File(tempdir, System.currentTimeMillis() + ".csv");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
//		if (csvFile.exists()) {
//			csvFile.deleteOnExit();
//		}
	}

	@Test
	void test() throws Exception {
		PortfolioCSVMapper mapper = new PortfolioCSVMapper(csvFile);
		
		List<PortfolioEntry> list = new ArrayList<PortfolioEntry>();
		for (int i = 0; i < 10; i++) {
			list.add(getPortfolioEntry());
		}
		
		mapper.mapPortfolioCSV(list);
	}
	
	private PortfolioEntry getPortfolioEntry() {
		PortfolioEntry entry = new PortfolioEntry();
		
		Random rand = new Random();
		int randInt = rand.nextInt(100);
		
		entry.setSymbol("Symbol" + randInt);
		entry.setName("Name" + randInt);
		entry.setPrice(rand.nextDouble());
		entry.setQuantity(rand.nextDouble());
		entry.setTotal(rand.nextDouble());
		entry.setCostBasis(rand.nextDouble());
		entry.setGain(rand.nextDouble());
		entry.setGainPercentage(rand.nextDouble());
		
		return entry;
	}

}
