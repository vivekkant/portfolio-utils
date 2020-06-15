package org.weekendsoft.portfolioutil.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.Nav;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;
import org.weekendsoft.portfolioutil.model.Quote;
import org.weekendsoft.portfolioutil.util.PortfolioCSVMapper;
import org.weekendsoft.portfolioutil.util.PortfolioCSVParser;
import org.weekendsoft.portfolioutil.util.SymbolSourceIdentifier;

public class PortfolioFileUpdater {

	private static final Logger LOG = Logger.getLogger(PortfolioFileUpdater.class);
	
	public void updatePortfolioFile(File in, File out) throws Exception {
		
		
		List<PortfolioEntry> list = null;
		if (!in.exists()) {
			LOG.error("Input file does not exist :" + in);
			throw new Exception("Input file does not exist");
		}
		else {
			PortfolioCSVParser parser = new PortfolioCSVParser(in);
			list = parser.parsePortfolioCSV();
			LOG.debug("Parsed file " + in);
			LOG.debug("Obtained portfolio record list " + Arrays.toString(list.toArray()));
		}
		
		Map<String, PortfolioEntry> portfolio = updatePortfolio(list);
		
		PortfolioCSVMapper mapper = new PortfolioCSVMapper(out);
		mapper.mapPortfolioCSV(new ArrayList<PortfolioEntry>(portfolio.values()));
		
	}
	
	
	public Map<String, PortfolioEntry> updatePortfolio(List<PortfolioEntry> list ) throws Exception {
		
		Map<String, PortfolioEntry> portfolio = new HashMap<String, PortfolioEntry>();
		
		List<Integer> amfiCodes = new ArrayList<Integer>();
		List<String> yahooSymbols = new ArrayList<String>();
		for(PortfolioEntry entry : list) {
			
			int code;
			if ((code = SymbolSourceIdentifier.isAMFISource(entry.getSymbol())) != -1) {
				amfiCodes.add(code);
			}
			else if (SymbolSourceIdentifier.isYahooSource(entry.getSymbol())) {
				yahooSymbols.add(entry.getSymbol());
			}
			else {
				entry.setPrice(-1);
			}
			
			portfolio.put(entry.getSymbol(), entry);
			
		}
		
		if (amfiCodes.size() > 0) {
			AMFINavDownloader navDownloader = new  AMFINavDownloader();
			Map<Integer, Nav> navs = navDownloader.downloadNavs(amfiCodes);
			updateMutulFundsInPortfolio(portfolio, navs);
		}
		
		if (yahooSymbols.size() > 0) {
			YahooFinanceQuoteDownloader quoteDownloader = new YahooFinanceQuoteDownloader();
			Map<String, Quote> quotes = quoteDownloader.downloadQuotes(yahooSymbols);
			updateStockInPortfolio(portfolio, quotes);
		}
		
		return portfolio;
	}
	
	private void updateMutulFundsInPortfolio(Map<String, PortfolioEntry> portfolio, Map<Integer, Nav> navs) {
		
		for (int code : navs.keySet()) {

			Nav nav = navs.get(code);
			
			PortfolioEntry entry = updatePortfolioEntry(portfolio.get(Integer.toString(code)), 
														nav.getName(), 
														nav.getNav());
			
			portfolio.put(Integer.toString(code), entry);
			LOG.debug("Updated entry : " + entry);
		}
		
	}
	
	private void updateStockInPortfolio(Map<String, PortfolioEntry> portfolio, Map<String, Quote> quotes) {
		
		for (String symbol : quotes.keySet()) {
			
			Quote quote = quotes.get(symbol);
			PortfolioEntry entry = updatePortfolioEntry(portfolio.get(symbol), 
														quote.getLongName(), 
														quote.getRegularMarketPrice());
			
			portfolio.put(symbol, entry);
			LOG.debug("Updated entry : " + entry);
		}
		
	}
	
	private PortfolioEntry updatePortfolioEntry(PortfolioEntry entry, String name, double price) {
		
		LOG.debug("Updateding entry " + name + "  with price " + price);
		
		entry.setPrice(price);
		entry.setName(name);
		
		double total = entry.getQuantity() * price;
		double gain = total - entry.getCostBasis();
		double gainPC = (gain * 100) / entry.getCostBasis();
		
		entry.setTotal(total);
		entry.setGain(gain);
		entry.setGainPercentage(gainPC);
		
		return entry;
	}
	
}
