package org.weekendsoft.portfolioutil.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;
import org.weekendsoft.portfolioutil.model.Price;
import org.weekendsoft.portfolioutil.util.PortfolioCSVMapper;
import org.weekendsoft.portfolioutil.util.PortfolioCSVParser;
import org.weekendsoft.portfolioutil.util.PortfolioEmailMapper;
import org.weekendsoft.portfolioutil.util.PortfolioMapper;
import org.weekendsoft.portfolioutil.util.SymbolSourceIdentifier;

public class PortfolioFileUpdater {

	private static final Logger LOG = Logger.getLogger(PortfolioFileUpdater.class);
	
	private double costTotal = 0;
	private double valueTotal = 0;
	
	public void updatePortfolioFile(File in, File out, String email) throws Exception {
		
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
		List<PortfolioEntry> entries = getPortfolioEntryList(portfolio);
		
		PortfolioMapper csvMapper = new PortfolioCSVMapper(out);
		csvMapper.mapPortfolio(entries);
		
		if (email != null) {
			try {
				PortfolioMapper eMapper = new PortfolioEmailMapper(email, getEmailSubject(in.getName()));
				eMapper.mapPortfolio(entries);
			} catch (Exception e) {
				LOG.error("Email sending failed", e);
			}
		}
		
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
				updateZeroValueEntry(entry, "Symbol does not match any format");
			}
			
			portfolio.put(entry.getSymbol(), entry);
			
		}
		
		if (amfiCodes.size() > 0) {
			Downloader amfiDownloader = new  AMFINavDownloader();
			downloadAndUpdate(portfolio, amfiDownloader, amfiCodes);
		}
		
		if (yahooSymbols.size() > 0) {
			Downloader yahooDownloader = new YahooFinanceQuoteDownloader();
			downloadAndUpdate(portfolio, yahooDownloader, yahooSymbols);
		}
		
		if (iciciPruSymbols.size() > 0) {
			Downloader iciciPruDownloader = new ICICIPruLifeDownloader();
			downloadAndUpdate(portfolio, iciciPruDownloader, iciciPruSymbols);
		}
		
		if (bbSymbols.size() > 0) {
			Downloader bbDownloader = new BankBazaarGoldPriceDownloader();
			downloadAndUpdate(portfolio, bbDownloader, bbSymbols);
		}
		
		return portfolio;
	}
	
	private void downloadAndUpdate(Map<String, PortfolioEntry> portfolio, 
									Downloader downloader, List<String> symbols) {
		try {
			
			Map<String, Price> prices = downloader.download(symbols);
			for (String symbol : prices.keySet()) {

				Price price = prices.get(symbol);
				
				PortfolioEntry entry = updatePortfolioEntry(portfolio.get(symbol), 
															price.getName(), 
															price.getPrice());
				portfolio.put(symbol, entry);
				LOG.debug("Updated entry : " + entry);
			}		
			
		} 
		catch (Exception e) {
			
			LOG.error("Exception while downloading", e);
			
			for (String symbol : symbols) {

				PortfolioEntry entry = portfolio.get(symbol);
				if (entry != null) {
					updateZeroValueEntry(entry, "Enable to download price");
					portfolio.put(symbol, entry);
				}
			}		
		}
	}
	
	private void updateZeroValueEntry(PortfolioEntry entry, String comments) {
		entry.setCostPrice(entry.getQuantity() > 0 ? 
						   entry.getCostBasis() / entry.getQuantity() :
						   0);
		entry.setPrice(entry.getCostPrice());
		entry.setTotal(entry.getCostBasis());
		entry.setComments(comments);
	}
	
	
	private PortfolioEntry updatePortfolioEntry(PortfolioEntry entry, 
												String name, double price) {
		
		LOG.debug("Updateding entry " + name + "  with price " + price);
		
		entry.setPrice(price);
		entry.setName(name);
		
		double total = entry.getQuantity() * price;
		double gain = total - entry.getCostBasis();
		double gainPC = (gain * 100) / entry.getCostBasis();
		double costPrice = entry.getQuantity() > 0 ? 
				entry.getCostBasis() / entry.getQuantity() :
				0;
		
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
		System.out.println("Date : " + new Date());
		System.out.println("-----------------------------------------");
		System.out.println("Investment value : " + PortfolioCSVMapper.formatDouble(this.costTotal));
		System.out.println("Current value : " + PortfolioCSVMapper.formatDouble(this.valueTotal));
		System.out.println("Gain : " + PortfolioCSVMapper.formatDouble(gain));
		System.out.println("Gain % : " + PortfolioCSVMapper.formatDouble(gainPc));
		System.out.println("-----------------------------------------");	
	}
	
	private String getEmailSubject(String fileName) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		StringBuilder buf = new StringBuilder("Portfolio Update for ");
		buf.append(fileName);
		buf.append(" ");
		buf.append(sdf.format(new Date()));
		
		return buf.toString();
	}
	
}
