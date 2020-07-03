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
	
	private double costTotal = 0;
	private double valueTotal = 0;
	
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
		List<PortfolioEntry> entries = getPortfolioEntryList(portfolio);
		mapper.mapPortfolioCSV(entries);
		
		printPortfolioSummary(in.getName());
	}
	
	
	public Map<String, PortfolioEntry> updatePortfolio(List<PortfolioEntry> list) throws Exception {
		
		Map<String, PortfolioEntry> portfolio = new HashMap<String, PortfolioEntry>();
		
		List<String> amfiCodes = new ArrayList<String>();
		List<String> yahooSymbols = new ArrayList<String>();
		List<String> iciciPruSymbols = new ArrayList<String>();
		
		for(PortfolioEntry entry : list) {
			
			String code;
			if ((code = SymbolSourceIdentifier.isAMFISource(entry.getSymbol())) != null) {
				amfiCodes.add(code);
			}
			else if (SymbolSourceIdentifier.isYahooSource(entry.getSymbol())) {
				yahooSymbols.add(entry.getSymbol());
			}
			else if (SymbolSourceIdentifier.isICICIPruSource(entry.getSymbol())) {
				iciciPruSymbols.add(entry.getSymbol());
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
		
		if (iciciPruSymbols.size() > 0) {
			ICICIPruLifeDownloader navDownloader = new ICICIPruLifeDownloader();
			Map<String, Nav> navs = navDownloader.downloadNavs(iciciPruSymbols);
			updateMutulFundsInPortfolio(portfolio, navs);
			
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
	
	private void updateStocksInPortfolio(Map<String, PortfolioEntry> portfolio, 
										 Map<String, Quote> quotes) {
		
		for (String symbol : quotes.keySet()) {
			
			Quote quote = quotes.get(symbol);
			PortfolioEntry entry = updatePortfolioEntry(portfolio.get(symbol), 
														quote.getLongName(), 
														quote.getRegularMarketPrice());
			
			portfolio.put(symbol, entry);
			LOG.debug("Updated entry : " + entry);
		}
		
	}
	
	private PortfolioEntry updatePortfolioEntry(PortfolioEntry entry, 
												String name, double price) {
		
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
	
	List<PortfolioEntry> getPortfolioEntryList(Map<String, PortfolioEntry> map) {
		
		List<PortfolioEntry> list = new ArrayList<PortfolioEntry>();
		
		for(PortfolioEntry entry : map.values()) {
			
			list.add(entry);
			this.costTotal += entry.getCostBasis();
			this.valueTotal += entry.getTotal();
		}
		
		double gain = valueTotal - costTotal;
		double gainPc = (gain * 100) / costTotal;
		
		PortfolioEntry entry = new PortfolioEntry();
		entry.setSymbol("");
		entry.setName("Total");
		entry.setTotal(this.valueTotal);
		entry.setCostBasis(this.costTotal);
		entry.setGain(gain);
		entry.setGainPercentage(gainPc);
		
		list.add(entry);
		
		return list;
	}
	
	private void printPortfolioSummary(String fileName) {
		
		double gain = this.valueTotal - this.costTotal;
		double gainPc = (gain * 100) / this.costTotal;
		
		System.out.println("-----------------------------------------");
		System.out.println("File : " + fileName);
		System.out.println("-----------------------------------------");
		System.out.println("Investment value : " + PortfolioCSVMapper.formatDouble(this.costTotal));
		System.out.println("Current value : " + PortfolioCSVMapper.formatDouble(this.valueTotal));
		System.out.println("Gain : " + PortfolioCSVMapper.formatDouble(gain));
		System.out.println("Gain % : " + PortfolioCSVMapper.formatDouble(gainPc));
		System.out.println("-----------------------------------------");	
	}
	
}
