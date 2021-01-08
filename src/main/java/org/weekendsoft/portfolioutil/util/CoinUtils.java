package org.weekendsoft.portfolioutil.util;

import java.util.List;

import org.weekendsoft.portfolioutil.model.PortfolioEntry;
import org.weekendsoft.zerodhascraper.model.CoinEntry;

public class CoinUtils {
	
	public static PortfolioEntry map(CoinEntry coin) {
		
		PortfolioEntry entry = new PortfolioEntry();
		entry.setSymbol("");
		entry.setName(coin.getName());
		entry.setPrice(coin.getNav());
		entry.setQuantity(coin.getUnit());
		entry.setTotal(coin.getCurrent());
		entry.setCostBasis(coin.getInvestment());
		entry.setCostPrice(coin.getInvestment()/coin.getUnit());
		entry.setGain(coin.getGain());
		entry.setGainPercentage(coin.getGainper());
		entry.setComments("");
		
		return entry;
	}
	
	public static PortfolioEntry total(List<CoinEntry> list) {
		
		PortfolioEntry entry = new PortfolioEntry();
		
		double total = 0;
		double cost = 0;
		for (CoinEntry coin : list) {
			total += coin.getCurrent();
			cost += coin.getInvestment();
		}
		
		entry.setSymbol("");
		entry.setName("Total");
		entry.setCostBasis(cost);
		entry.setTotal(total);
		entry.setGain(total - cost);
		entry.setGainPercentage((entry.getGain() * 100) / entry.getCostBasis());
		
		return entry;
	}

}
