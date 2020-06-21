package org.weekendsoft.portfolioutil.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PortfolioFileUpdaterTest {
	
	static File inFile = null;
	static File outFile = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//File tempdir = new File(System.getProperty("java.io.tmpdir"));
		File tempdir = new File("/Users/vivekkant/Downloads");
		String filename = System.currentTimeMillis() + ".csv";
		inFile = new File(tempdir, filename);
		outFile = new File(tempdir, "out_" +filename);
		writeSampleFile(inFile);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void test() throws Exception {
		PortfolioFileUpdater updater = new PortfolioFileUpdater();
		updater.updatePortfolioFile(inFile, outFile);
	}
	
	static void writeSampleFile(File file) throws IOException {
		
		StringBuffer buf = new StringBuffer();
		buf.append("\"Symbol\",\"Name\",\"Price\",\"Quantity\",\"Total\",\"Cost basis\",\"Gain\",\"% Gain\"\n" + 
				"\"DMART.NS\",\"Avenue Supermarts Limited\",\"2306.5500\",\"53.0000\",\"122247.1500\",\"107938.0900\",\"14309.0600\",\"13.2567\"\n" + 
				"\"120465\",\"Axis Bluechip Fund - Direct Plan - Growth\",\"30.1300\",\"3025.7190\",\"91164.9135\",\"100000.0229\",\"-8835.1094\",\"-8.8351\"\n" + 
				"\"146675\",\"Axis Overnight Fund - Direct Plan - Growth Option\",\"1057.8346\",\"874.9450\",\"925547.0941\",\"925000.0050\",\"547.0891\",\"0.0591\"\n" + 
				"\"125354\",\"Axis Small Cap Fund - Direct Plan - Growth\",\"28.7400\",\"2898.5510\",\"83304.3557\",\"100000.0095\",\"-16695.6538\",\"-16.6957\"\n" + 
				"\"BAJFINANCE.NS\",\"Bajaj Finance Limited\",\"1938.6000\",\"40.0000\",\"77544.0000\",\"105785.4000\",\"-28241.4000\",\"-26.6969\"\n" + 
				"\"BAJAJFINSV.NS\",\"Bajaj Finserv Limited\",\"4465.4500\",\"24.0000\",\"107170.8000\",\"110270.7006\",\"-3099.9006\",\"-2.8112\"\n" + 
				"\"118577\",\"Franklin India Liquid Fund\",\"2996.0584\",\"269.2850\",\"806793.5862\",\"797309.0336\",\"9484.5526\",\"1.1896\"\n" + 
				"\"118464\",\"IDFC  Government Securities Fund-  Investment Plan-Direct Plan-Growth\",\"27.2906\",\"11167.4850\",\"304767.3661\",\"299999.9890\",\"4767.3771\",\"1.5891\"\n" + 
				"\"139538\",\"Mahindra liquid Fund - Direct Plan -Growth\",\"1281.1800\",\"312.2130\",\"400001.0513\",\"400000.4894\",\"0.5619\",\"0.0001\"\n" + 
				"\"NIFTYBEES.NS\",\"Nifty BeES\",\"93.9300\",\"200.0000\",\"18786.0000\",\"18157.0500\",\"628.9500\",\"3.4639\"\n" + 
				"\"118672\",\"Nippon India Gilt Securities Fund - Direct Plan Growth Plan - Growth Option\",\"31.0609\",\"6492.4500\",\"201661.3402\",\"199999.9929\",\"1661.3473\",\"0.8307\"\n" + 
				"\"122639\",\"Parag Parikh Long Term Equity Fund - Direct Plan - Growth\",\"24.3901\",\"3849.7220\",\"93895.1046\",\"99999.9937\",\"-6104.8891\",\"-6.1049\"\n" + 
				"\"120837\",\"Quant Liquid Plan-Growth Option-Direct Plan\",\"31.2000\",\"16023.1246\",\"499921.4875\",\"500000.0008\",\"-78.5133\",\"-0.0157\"\n" + 
				"\"119800\",\"SBI Liquid Fund - DIRECT PLAN -Growth\",\"3089.2300\",\"467.4970\",\"1444205.7573\",\"1447722.8691\",\"-3517.1118\",\"-0.2429\"\n" + 
				"\"119707\",\"SBI MAGNUM GILT FUND - DIRECT PLAN - GROWTH\",\"49.2460\",\"4092.6440\",\"201546.3464\",\"200000.0151\",\"1546.3313\",\"0.7732\"\n" + 
				"\"TATACONSUM.NS\",\"Tata Global Beverages Limited\",\"365.9000\",\"370.0000\",\"135383.0000\",\"115583.6400\",\"19799.3600\",\"17.1299\"\n" + 
				"\"UJJIVAN.NS\",\"Ujjivan Financial Services Limited\",\"153.6000\",\"532.0000\",\"81715.2000\",\"115292.5058\",\"-33577.3058\",\"-29.1236\"\n" + 
				"\"120792\",\"UTI - GILT FUND - Direct Plan - Growth Option\",\"48.2339\",\"4189.1310\",\"202058.1257\",\"200000.0057\",\"2058.1200\",\"1.0291\"\n" + 
				"\"WHIRLPOOL.NS\",\"Whirlpool of India Limited\",\"1854.6500\",\"52.0000\",\"96441.8000\",\"101530.2100\",\"-5088.4100\",\"-5.0117\"\n" + 
				"\"\",\"\",\"\",\"\",\"5894154.4786\",\"5944590.0231\",\"-50435.5445\",\"-0.8484\"");
		
		FileWriter writer = new FileWriter(file);
		writer.append(buf.toString());
		writer.flush();
		writer.close();
	}

}
