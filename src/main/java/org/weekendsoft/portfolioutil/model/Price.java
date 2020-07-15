package org.weekendsoft.portfolioutil.model;

import java.util.Date;

public class Price {
	
	private String symbol;
	private String name;
	private double price;
	private Date date;
	
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
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Price [symbol=" + symbol + ", name=" + name + ", price=" + price + ", date=" + date + "]";
	}
	
	

}
