package org.weekendsoft.portfolioutil.model;

import java.util.Date;

public class Nav {
	
	private int code;
	private String isin;
	private String isin2;
	private String name;
	private float nav;
	private Date date;
	
	
	
	public int getCode() {
		return code;
	}



	public void setCode(int code) {
		this.code = code;
	}



	public String getIsin() {
		return isin;
	}



	public void setIsin(String isin) {
		this.isin = isin;
	}



	public String getIsin2() {
		return isin2;
	}



	public void setIsin2(String isin2) {
		this.isin2 = isin2;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public float getNav() {
		return nav;
	}



	public void setNav(float nav) {
		this.nav = nav;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	@Override
	public String toString() {
		return "Nav [code=" + code + ", isin=" + isin + ", isin2=" + isin2 + ", name=" + name + ", nav=" + nav
				+ ", date=" + date + "]";
	}
	
	

}
