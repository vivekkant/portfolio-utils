package org.weekendsoft.portfolioutil.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;

class PortfolioEmailMapperTest {


	@Test
	void test() throws Exception {
		PortfolioMapper mapper = new PortfolioEmailMapper("vivek.kant@gmail.com", "Test Portfolio Email");
		
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
