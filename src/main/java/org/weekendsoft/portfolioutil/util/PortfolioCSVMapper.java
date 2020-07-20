package org.weekendsoft.portfolioutil.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;

public class PortfolioCSVMapper {
	
	private static final Logger LOG = Logger.getLogger(PortfolioCSVMapper.class);
	private static final DecimalFormat format = new DecimalFormat("#.####");
	
	private File dest;
	
	public PortfolioCSVMapper(File dest) {
		this.dest = dest;
		LOG.debug("CSV Source is " + dest);
	}
	
	public void mapPortfolioCSV(List<PortfolioEntry> list) throws Exception {
		
		LOG.debug("Writing CSV File : " + dest);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.EXCEL
				.withHeader("Symbol","Name","Cost Price","Price","Quantity","Cost basis","Total","Gain","% Gain","Comments")
				.withQuote('"').withQuoteMode(QuoteMode.ALL)
				);
		
		for (PortfolioEntry entry : list) {
			printer.printRecord(
						entry.getSymbol(),
						entry.getName(),
						formatDouble(entry.getCostPrice()),
						formatDouble(entry.getPrice()),
						formatDouble(entry.getQuantity()),
						formatDouble(entry.getCostBasis()),
						formatDouble(entry.getTotal()),
						formatDouble(entry.getGain()),
						formatDouble(entry.getGainPercentage()),
						entry.getComments() == null ? "" : entry.getComments()
					);
			LOG.debug("Added record : " + entry);
		}
		
		printer.flush();
		printer.close();
	}
	
	public static String formatDouble(double num) {
		return format.format(num);
	}
	
}
