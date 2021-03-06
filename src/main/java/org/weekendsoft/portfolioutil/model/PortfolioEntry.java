package org.weekendsoft.portfolioutil.model;

public class PortfolioEntry {
	
	private String symbol;
	private String name;
	private double price;
	private double quantity;
	private double total;
	private double costBasis;
	private double costPrice;
	private double gain;
	private double gainPercentage;
	private String comments;
	
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getCostBasis() {
		return costBasis;
	}

	public void setCostBasis(double costBasis) {
		this.costBasis = costBasis;
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	public double getGainPercentage() {
		return gainPercentage;
	}

	public void setGainPercentage(double gainPercentage) {
		this.gainPercentage = gainPercentage;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "PortfolioEntry [symbol=" + symbol + ", name=" + name + ", price=" + price + ", quantity=" + quantity
				+ ", total=" + total + ", costBasis=" + costBasis + ", costPrice=" + costPrice + ", gain=" + gain
				+ ", gainPercentage=" + gainPercentage + ", comments=" + comments + "]";
	}



}
