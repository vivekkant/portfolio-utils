package org.weekendsoft.portfolioutil.service;

import java.util.List;
import java.util.Map;

import org.weekendsoft.portfolioutil.model.Price;

public interface Downloader {
	
	public Map<String, Price> download(List<String> symbols) throws Exception;

}
