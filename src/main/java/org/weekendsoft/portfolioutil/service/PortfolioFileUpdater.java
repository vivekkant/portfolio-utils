package org.weekendsoft.portfolioutil.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;
import org.weekendsoft.portfolioutil.model.Price;
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
		
		Map<String, PortfolioEntry> portfolio = new TreeMap<String, PortfolioEntry>();
		
		List<String> amfiCodes = new ArrayList<String>();
		List<String> yahooSymbols = new ArrayList<String>();
		List<String> iciciPruSymbols = new ArrayList<String>();
		List<String> bbSymbols = new ArrayList<String>();
		
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
			else if (SymbolSourceIdentifier.isBBSource(entry.getSymbol())) {
				bbSymbols.add(entry.getSymbol());
			}
			else {
				entry.setPrice(0);
				entry.setCostPrice(entry.getCostBasis() / entry.getQuantity());
				entry.setTotal(0);
				entry.setComments("Symbol does not match any format");
			}
			
			portfolio.put(entry.getSymbol(), entry);
			
		}
		
		if (amfiCodes.size() > 0) {
			Downloader amfiDownloader = new  AMFINavDownloader();
			Map<String, Price> prices = amfiDownloader.download(amfiCodes);
			updateInPortfolio(portfolio, prices);
		}
		
		if (yahooSymbols.size() > 0) {
			Downloader yahooDownloader = new YahooFinanceQuoteDownloader();
			Map<String, Price> prices = yahooDownloader.download(yahooSymbols);
			updateInPortfolio(portfolio, prices);
		}
		
		if (iciciPruSymbols.size() > 0) {
			Downloader iciciPruDownloader = new ICICIPruLifeDownloader();
			Map<String, Price> prices = iciciPruDownloader.download(iciciPruSymbols);
			updateInPortfolio(portfolio, prices);
		}
		
		if (bbSymbols.size() > 0) {
			Downloader bbDownloader = new BankBazaarGoldPriceDownloader();
			Map<String, Price> prices = bbDownloader.download(bbSymbols);
			updateInPortfolio(portfolio, prices);
		}
		
		return portfolio;
	}
	
	private void updateInPortfolio(Map<String, PortfolioEntry> portfolio, Map<String, Price> prices) {
		
		for (String symbol : prices.keySet()) {

			Price price = prices.get(symbol);
			
			PortfolioEntry entry = updatePortfolioEntry(portfolio.get(symbol), 
														price.getName(), 
														price.getPrice());
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
		double costPrice = entry.getCostBasis() / entry.getQuantity();
		
		entry.setTotal(total);
		entry.setGain(gain);
		entry.setGainPercentage(gainPC);
		entry.setCostPrice(costPrice);
		
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
