package org.weekendsoft.portfolioutil.model;

public class Quote {
	
	private String 	symbol;
	private String 	longName;
	private float 	regularMarketPrice;
	private float 	regularMarketPreviousClose;

	
	public String getLongName() {
		return longName;
	}
	
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public float getRegularMarketPrice() {
		return regularMarketPrice;
	}
	
	public void setRegularMarketPrice(float regularMarketPrice) {
		this.regularMarketPrice = regularMarketPrice;
	}
	
	public float getRegularMarketPreviousClose() {
		return regularMarketPreviousClose;
	}
	
	public void setRegularMarketPreviousClose(float regularMarketPreviousClose) {
		this.regularMarketPreviousClose = regularMarketPreviousClose;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "Quote [longName=" + longName + ", regularMarketPrice=" + regularMarketPrice
				+ ", regularMarketPreviousClose=" + regularMarketPreviousClose + ", symbol=" + symbol + "]";
	}
	

	
	

}
