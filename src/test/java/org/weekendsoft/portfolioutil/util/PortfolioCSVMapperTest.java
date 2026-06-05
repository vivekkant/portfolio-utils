package org.weekendsoft.portfolioutil.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;

class PortfolioCSVMapperTest {
	
	static File csvFile = null;

	@BeforeClass
	static void setUpBeforeClass() throws Exception {
		File tempdir = new File(System.getProperty("java.io.tmpdir"));
		csvFile = new File(tempdir, System.currentTimeMillis() + ".csv");
	}

	@AfterClass
	static void tearDownAfterClass() throws Exception {
		if (csvFile.exists()) {
			csvFile.deleteOnExit();
		}
	}

	@Test
	void test() throws Exception {
		PortfolioMapper mapper = new PortfolioCSVMapper(csvFile);
		
		List<PortfolioEntry> list = new ArrayList<PortfolioEntry>();
		for (int i = 0; i < 10; i++) {
			list.add(getPortfolioEntry());
		}
		
		mapper.mapPortfolio(list);
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
		entry.setCostPrice(rand.nextDouble());
		entry.setComments("Comments " + randInt);
		
		return entry;
	}

}
