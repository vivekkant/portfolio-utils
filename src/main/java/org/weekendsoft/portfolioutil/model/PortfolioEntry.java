package org.weekendsoft.portfolioutil.model;

public class PortfolioEntry {
	
	private String symbol;
	private String name;
	private String price;
	private String quantity;
	private String total;
	private String costBasis;
	private String gain;
	private String gainPercentage;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getCostBasis() {
		return costBasis;
	}
	public void setCostBasis(String costBasis) {
		this.costBasis = costBasis;
	}
	public String getGain() {
		return gain;
	}
	public void setGain(String gain) {
		this.gain = gain;
	}
	public String getGainPercentage() {
		return gainPercentage;
	}
	public void setGainPercentage(String gainPercentage) {
		this.gainPercentage = gainPercentage;
	}
	
	@Override
	public String toString() {
		return "PortfolioEntry [symbol=" + symbol + ", name=" + name + ", price=" + price + ", quantity=" + quantity
				+ ", total=" + total + ", costBasis=" + costBasis + ", gain=" + gain + ", gainPercentage="
				+ gainPercentage + "]";
	}
	
	

}
