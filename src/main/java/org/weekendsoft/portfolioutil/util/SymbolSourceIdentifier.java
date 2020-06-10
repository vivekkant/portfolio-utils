package org.weekendsoft.portfolioutil.util;

public class SymbolSourceIdentifier {
	
	public enum SymbolType {
	    YAHOO,
	    AMFI,
	    OTHER
	  }
	
	public static SymbolType identifySymbolSource(String symbol) {
		
		if (isAMFISource(symbol)) {
			return SymbolType.AMFI;
		}
		else if (isYahooSource(symbol)) {
			return SymbolType.YAHOO;
		}
		else {
			return SymbolType.OTHER;
		}
	}
	
	public static boolean isAMFISource(String symbol) {
		
		if (symbol != null && symbol.trim().length() == 6) {
			try {
				Integer.parseInt(symbol.trim());
				return true;
			} catch (NumberFormatException e) {
			}
		}
		
		return false;
	}
	
	public static boolean isYahooSource(String symbol) {
		
		if (symbol != null && symbol.trim().length() > 3) {
			symbol = symbol.trim();
			if (symbol.indexOf('.') > 0) {
				String exchangeCode = symbol.substring(symbol.indexOf('.') + 1);
				if ("NS".equals(exchangeCode) || "BO".equals(exchangeCode)) {
					return true;
				}
			}
		}
		
		return false;
	}

}
