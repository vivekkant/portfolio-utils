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
		List<PortfolioEntry> entries = new ArrayList<PortfolioEntry>(portfolio.values());
		mapper.mapPortfolioCSV(entries);
		
		printPortfolioSummary(entries, in.getName());
	}
	
	
	public Map<String, PortfolioEntry> updatePortfolio(List<PortfolioEntry> list ) throws Exception {
		
		Map<String, PortfolioEntry> portfolio = new HashMap<String, PortfolioEntry>();
		
		List<String> amfiCodes = new ArrayList<String>();
		List<String> yahooSymbols = new ArrayList<String>();
		for(PortfolioEntry entry : list) {
			
			String code;
			if ((code = SymbolSourceIdentifier.isAMFISource(entry.getSymbol())) != null) {
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
			Map<String, Nav> navs = navDownloader.downloadNavs(amfiCodes);
			updateMutulFundsInPortfolio(portfolio, navs);
		}
		
		if (yahooSymbols.size() > 0) {
			YahooFinanceQuoteDownloader quoteDownloader = new YahooFinanceQuoteDownloader();
			Map<String, Quote> quotes = quoteDownloader.downloadQuotes(yahooSymbols);
			updateStocksInPortfolio(portfolio, quotes);
		}
		
		return portfolio;
	}
	
	private void updateMutulFundsInPortfolio(Map<String, PortfolioEntry> portfolio, Map<String, Nav> navs) {
		
		for (String code : navs.keySet()) {

			Nav nav = navs.get(code);
			
			PortfolioEntry entry = updatePortfolioEntry(portfolio.get(code), 
														nav.getName(), 
														nav.getNav());
			portfolio.put(code, entry);
			LOG.debug("Updated entry : " + entry);
		}
		
	}
	
	private void updateStocksInPortfolio(Map<String, PortfolioEntry> portfolio, Map<String, Quote> quotes) {
		
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
	
	private void printPortfolioSummary(List<PortfolioEntry> list, String fileName) {
		
		int i = 0;
		double costTotal = 0;
		double valueTotal = 0;		
		
		for(PortfolioEntry entry : list) {
			costTotal += entry.getCostBasis();
			valueTotal += entry.getTotal();
			i++;
		}
		
		double gain = valueTotal - costTotal;
		double gainPc = (gain * 100) / costTotal;
		
		System.out.println("-----------------------------------------");
		System.out.println("File : " + fileName);
		System.out.println("-----------------------------------------");
		System.out.println("Total no investments : " + i);
		System.out.println("Investment value : " + PortfolioCSVMapper.formatDouble(costTotal));
		System.out.println("Current value : " + PortfolioCSVMapper.formatDouble(valueTotal));
		System.out.println("Gain : " + PortfolioCSVMapper.formatDouble(gain));
		System.out.println("Gain % : " + PortfolioCSVMapper.formatDouble(gainPc));
		System.out.println("-----------------------------------------");	
	}
	
}
