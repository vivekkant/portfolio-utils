package org.weekendsoft.portfolioutil.util;

import java.util.List;

import org.weekendsoft.portfolioutil.model.PortfolioEntry;

public interface PortfolioMapper {
	
	public void mapPortfolio(List<PortfolioEntry> list) throws Exception;

}
