package org.weekendsoft.portfolioutil.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;

class PortfolioCSVParserTest {
	
	static File csvFile = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File tempdir = new File(System.getProperty("java.io.tmpdir"));
		csvFile = new File(tempdir, System.currentTimeMillis() + ".csv");
		writeSampleFile(csvFile);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		if (csvFile.exists()) {
			csvFile.deleteOnExit();
		}
	}

	@Test
	void test() throws Exception {
		PortfolioCSVParser parser = new PortfolioCSVParser(csvFile);
		List<PortfolioEntry> entry = parser.parsePortfolioCSV();
		assertEquals(4, entry.size());
		
	}
	
	static void writeSampleFile(File file) throws IOException {
		
		StringBuffer buf = new StringBuffer();
		buf.append("\"Symbol\",\"Name\",\"Price\",\"Quantity\",\"Total\",\"Cost basis\",\"Gain\",\"% Gain\"\n" + 
				"\"104331\",\"Aditya Birla Sun Life Tax Plan - Regular Plan - Growth Option\",\"33.8900\",\"4670.8600\",\"158295.4454\",\"184732.5130\",\"-26437.0676\",\"-14.3110\"\n" + 
				"\"103360\",\"Franklin India Smaller Companies Fund-Growth\",\"36.7517\",\"1868.3310\",\"68664.3404\",\"102378.7470\",\"-33714.4066\",\"-32.9311\"\n" + 
				"\"105758\",\"HDFC Mid-Cap Opportunities Fund - Growth Option\",\"43.3740\",\"1915.5250\",\"83083.9814\",\"107813.4091\",\"-24729.4277\",\"-22.9372\"\n" + 
				"\"108466\",\"ICICI Prudential Bluechip Fund\",\"35.0900\",\"2852.2530\",\"100085.5578\",\"120336.5541\",\"-20250.9963\",\"-16.8286\"\n" + 
				"\"\",\"\",\"\",\"\",\"410129.3250\",\"515261.2232\",\"-105131.8982\",\"-20.4036\"");
		
		FileWriter writer = new FileWriter(file);
		writer.append(buf.toString());
		writer.flush();
		writer.close();
	}

}
