package org.weekendsoft.portfolioutil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.service.PortfolioFileUpdater;
import org.weekendsoft.portfolioutil.util.CSVFileFilter;

public class Driver {
	
	private static final Logger LOG = Logger.getLogger(Driver.class);
	
	private static final SimpleDateFormat dateprefix = new SimpleDateFormat("yyyy-MM-dd-");
	
	private Options options = 	new Options();
	private File indir 		= 	null;
	private File outdir 	= 	null;

	public static void main(String[] args) {
		
		Driver driver = new Driver();
		driver.drive(args);
	}
	
	private void drive(String[] args) {
		
		CommandLine cmd = null;	
		LOG.debug("Got arguments : " + Arrays.toString(args));
		
		try {
			cmd = setAndParseOptions(args);
			setInAndOutDir(cmd);
			
			PortfolioFileUpdater updater = new PortfolioFileUpdater();
			for (File infile : indir.listFiles(new CSVFileFilter())) {
				
				LOG.debug("Procesing file : " + infile.getAbsolutePath());
				File outfile = getOutfile(infile);
				LOG.debug("Outfile is : " + outfile.getAbsolutePath());
				
				updater.updatePortfolioFile(infile, outfile);
			}
			
		} 
		catch (ParseException e) {
			LOG.error("Invalid options: " + e.getMessage(), e);
			System.out.println("Invalid options: " + e.getMessage());
			printHelp();
		} 
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
			System.out.println(e.getMessage());
		}		
	}

	
	private void setInAndOutDir(CommandLine cmd) throws Exception {
		
		if (!cmd.hasOption('i')) throw new ParseException("Inout argument not present");
		if (!cmd.hasOption('o')) throw new ParseException("Output argument not present");
		
		String infileStr = cmd.getOptionValue('i');
		if (infileStr == null || "".equals(infileStr)) throw new Exception("Invalid Input Dir");
		this.indir = new File(infileStr);
		if (!indir.exists() || !indir.isDirectory()) throw new Exception("Inout Directory Does not Exists");
		
		String outfileStr = cmd.getOptionValue('o');
		if (outfileStr == null || "".equals(outfileStr)) throw new Exception("Invalid Output Dir");
		this.outdir = new File(outfileStr);
		if (!outdir.exists() || !outdir.isDirectory()) throw new Exception("Output Directory Does not Exists");

	}
	
	private CommandLine setAndParseOptions(String[] args) throws ParseException {
		options.addOption("i", "indir", 	true, 	"Input directory with portoflio files");
		options.addOption("o", "outdir", 	true, 	"Output directory where portfolio files will be stored");
		options.addOption("h", "help", 		false, 	"Help");		
		
		CommandLineParser 	parser 	= new DefaultParser();
		CommandLine 		cmd 	= parser.parse( options, args);
		
		return cmd;

	}
	
	private void printHelp() {
	     HelpFormatter formatter = new HelpFormatter();
	      formatter.printHelp("FileNameSorter", options);
	}
	
	private File getOutfile(File infile) {
		String outfilename = dateprefix.format(new Date()) + infile.getName();
		return new File(outdir, outfilename);
	}

}
