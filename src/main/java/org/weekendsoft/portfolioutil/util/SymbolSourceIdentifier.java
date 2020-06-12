package org.weekendsoft.portfolioutil.util;

import org.apache.log4j.Logger;

public class SymbolSourceIdentifier {
	
	private static final Logger LOG = Logger.getLogger(SymbolSourceIdentifier.class);
	
	public enum SymbolType {
	    YAHOO,
	    AMFI,
	    OTHER
	  }
	
	public static SymbolType identifySymbolSource(String symbol) {
		
		LOG.debug("Analyzing symbol : " + symbol );
		
		if (isAMFISource(symbol) != -1) {
			return SymbolType.AMFI;
		}
		else if (isYahooSource(symbol)) {
			return SymbolType.YAHOO;
		}
		else {
			return SymbolType.OTHER;
		}
	}
	
	public static int isAMFISource(String symbol) {
		
		if (symbol != null && symbol.trim().length() == 6) {
			try {
				int code = Integer.parseInt(symbol.trim());
				LOG.debug("Symbol " + symbol + " is of AMFI");
				return code;
				
			} catch (NumberFormatException e) {
			}
		}
		
		return -1;
	}
	
	public static boolean isYahooSource(String symbol) {
		
		if (symbol != null && symbol.trim().length() > 3) {
			symbol = symbol.trim();
			if (symbol.indexOf('.') > 0) {
				String exchangeCode = symbol.substring(symbol.indexOf('.') + 1);
				if ("NS".equals(exchangeCode) || "BO".equals(exchangeCode)) {
					LOG.debug("Symbol " + symbol + " is of YAHOO");
					return true;
				}
			}
		}
		
		return false;
	}

}
