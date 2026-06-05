package org.weekendsoft.portfolioutil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


class DriverTest {
	
	static File indir = null;
	static File outdir = null;
	
	static File infile = null;
	
	private static final SimpleDateFormat dateprefix = new SimpleDateFormat("yyyy-MM-dd");

	@BeforeClass
	static void setUpBeforeClass() throws Exception {
		File tempdir = new File(System.getProperty("java.io.tmpdir"));
		indir = new File(tempdir, "portin");
		outdir = new File(tempdir, "portout");
		indir.mkdir();
		outdir.mkdir();
		infile = new File(indir, "portfolio.csv");
		writeSampleFile(infile);
	}

	@AfterClass
	static void tearDownAfterClass() throws Exception {
		indir.deleteOnExit();
		outdir.deleteOnExit();
	}

	@Test
	void testOptions() {
		String[] args = {"-i", indir.getPath(),
						 "-o", outdir.getPath(),
						 "-e", "vivek.kant@gmail.com"};
		Driver.main(args);

		File outdirnew = new File(outdir, dateprefix.format(new Date()));
		File outfile = new File(outdirnew, infile.getName());
		
		Assert.assertTrue(outfile.exists());
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
