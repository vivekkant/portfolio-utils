package org.weekendsoft.portfolioutil.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weekendsoft.portfolioutil.model.Price;

public class NSESGBDownloader implements Downloader {

	private static final Logger LOG = LoggerFactory.getLogger(NSESGBDownloader.class);

	private static final int SYMBOL_COL = 0;
	private static final int PREV_CLOSE_COL = 6;

	private final String filePath;

	public NSESGBDownloader(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public Map<String, Price> download(List<String> symbols) throws Exception {

		Map<String, Price> result = new HashMap<>();
		Map<String, Double> csvData = parseCSV();

		for (String symbol : symbols) {
			if (csvData.containsKey(symbol)) {
				Price price = new Price();
				price.setSymbol(symbol);
				price.setName(symbol);
				price.setPrice(csvData.get(symbol));
				price.setDate(new Date());
				result.put(symbol, price);
				LOG.debug("Found price for {}: {}", symbol, price.getPrice());
			} else {
				LOG.warn("Symbol not found in CSV: {}", symbol);
			}
		}

		return result;
	}

	private static final String UTF8_BOM = "\uFEFF";

	private Map<String, Double> parseCSV() throws IOException {
		Map<String, Double> data = new HashMap<>();
		boolean firstLine = true;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				if (firstLine) {
					line = line.replace(UTF8_BOM, "");
					firstLine = false;
				}
				String[] columns = parseCSVLine(line);
				if (columns.length > PREV_CLOSE_COL) {
					String symbol = columns[SYMBOL_COL].trim();
					String priceStr = columns[PREV_CLOSE_COL].replace(",", "").trim();
					LOG.debug("found symbol {} with price {}", symbol, priceStr);
					try {
						double price = Double.parseDouble(priceStr);
						data.put(symbol, price);
					} catch (NumberFormatException e) {
						LOG.warn("Failed to parse price for symbol {}: {}", symbol, priceStr);
					}
				}
			}
		}

		LOG.info("Loaded {} symbols from CSV", data.size());
		return data;
	}

	private String[] parseCSVLine(String line) {
		List<String> fields = new java.util.ArrayList<>();
		StringBuilder current = new StringBuilder();
		boolean inQuotes = false;

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == '"') {
				inQuotes = !inQuotes;
			} else if (c == ',' && !inQuotes) {
				fields.add(current.toString());
				current = new StringBuilder();
			} else {
				current.append(c);
			}
		}
		fields.add(current.toString());

		return fields.toArray(new String[0]);
	}

}
