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
		File tempdir = new File(System.getProperty("java.io.tmpdir"));
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
				"\"BAJFINANCE.NS\",\"Bajaj Finance Limited\",\"1895.3000\",\"10.0000\",\"45487.2000\",\"18950.0000\",\"-47712.8000\",\"-51.1940\"\n" + 
				"\"118535\",\"Franklin India Equity Fund GROWTH\",\"457.0593\",\"10.0000\",\"4570\",\"-21050.1335\",\"21050.1335\",\"0.0000\"\n" + 
				"\"5GTH\",\"Maximizer V\",\"19.0468\",\"100.0000\",\"1904.68\",\"1300000.0007\",\"-70401.0662\",\"-5.4155\"\n" + 
				"\"\",\"\",\"\",\"\",\"4409952.0955\",\"3971365.4395\",\"438586.6560\",\"11.0437\"");
		
		FileWriter writer = new FileWriter(file);
		writer.append(buf.toString());
		writer.flush();
		writer.close();
	}

}
