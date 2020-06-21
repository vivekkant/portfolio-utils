package org.weekendsoft.portfolioutil.util;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;

public class PortfolioCSVParser {
	
	private static final Logger LOG = Logger.getLogger(PortfolioCSVParser.class);
	
	private File source;
	
	public PortfolioCSVParser(File source) {
		this.source = source;
		LOG.debug("CSV Source is " + source);
	}
	
	
	public List<PortfolioEntry> parsePortfolioCSV() throws Exception {
		
		List<PortfolioEntry> list = new ArrayList<PortfolioEntry>();
		
		Reader reader = new FileReader(source);
		CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL
				.withFirstRecordAsHeader()
				.withIgnoreEmptyLines()
				.withIgnoreHeaderCase()
				.withTrim()
				);
		
		for (CSVRecord csvRecord : parser) {
			PortfolioEntry entry = parseCSVRecord(csvRecord);
			if (entry != null) {
				list.add(entry);
			}
		}
		
		parser.close();
		
		return list;
	}
	
	private PortfolioEntry parseCSVRecord(CSVRecord record) {
		
		PortfolioEntry entry = null;
		
		String symbol = record.get("Symbol");
		
		if (symbol != null && !"".equals(symbol)) {
			entry = new PortfolioEntry();
			
			entry.setSymbol(symbol);
			entry.setName(record.get("Name"));
			entry.setPrice(convertStringToBoolean(record.get("Price")));
			entry.setQuantity(convertStringToBoolean(record.get("Quantity")));
			entry.setTotal(convertStringToBoolean(record.get("Total")));
			entry.setCostBasis(convertStringToBoolean(record.get("Cost basis")));
			entry.setGain(convertStringToBoolean(record.get("Gain")));
			entry.setGainPercentage(convertStringToBoolean(record.get("% Gain")));
			
			LOG.debug("Parsed Record : " + entry);
		}
		else {
			LOG.info("Discarding CSV Record : " + record);
		}
		
		return entry;
	}
	
	private double convertStringToBoolean(String num) {
		double returnValue = -1;
		
		try {
			returnValue = Double.parseDouble(num);
		} catch (NumberFormatException e) {
		}
		
		return returnValue;
	}

}
