package org.weekendsoft.portfolioutil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;
import org.weekendsoft.portfolioutil.util.CoinUtils;
import org.weekendsoft.portfolioutil.util.PortfolioCSVMapper;
import org.weekendsoft.portfolioutil.util.PortfolioEmailMapper;
import org.weekendsoft.zerodhascraper.model.CoinEntry;
import org.weekendsoft.zerodhascraper.rpa.CoinPortfolioDownload;

public class CoinRPADriver {
	
	private static final Logger LOG = Logger.getLogger(CoinRPADriver.class);
	
	private static final SimpleDateFormat dateprefix = new SimpleDateFormat("yyyy-MM-dd");
	
	private Options options = 	new Options();
	private File 	outdir 	= 	null;
	private String 	email 	= 	null;
	private String username = null;
	private String password = null;
	private String pin = null;

	public static void main(String[] args) {
		
		CoinRPADriver driver = new CoinRPADriver();
		driver.drive(args);
		
		System.exit(0);
	}
	
	private void drive(String[] args) {
		
		CommandLine cmd = null;	
		LOG.debug("Got arguments : " + Arrays.toString(args));
		
		try {
			cmd = setAndParseOptions(args);
			setCMDParameters(cmd);
			
			//TODO Do the magic of parsing and send email
			CoinPortfolioDownload downloader = new CoinPortfolioDownload(this.username, 
																   this.password, 
																   this.pin);
			LOG.debug("Starting the RPA for Coin Username: " + this.username);
			List<CoinEntry> list = downloader.downloadCoinPortfolio();
			if (list.size() <= 0) {
				throw new Exception("RPA failed to execute");
			}
			LOG.info("Got portfolio list: " + list.size()); 
			LOG.debug("Got portfolio: " + list);
			
			List<PortfolioEntry> portfolio = new ArrayList<PortfolioEntry>();
			for (CoinEntry coin : list) {
				PortfolioEntry entry = CoinUtils.map(coin);
				portfolio.add(entry);
			}
			portfolio.add(CoinUtils.total(list));
			
			File dest = new File(outdir, "coin-portfolio-download.csv");
			PortfolioCSVMapper mapper = new PortfolioCSVMapper(dest);
			mapper.mapPortfolio(portfolio);
			
			PortfolioEmailMapper emapper = new PortfolioEmailMapper(this.email, 
												"Zerodha MF Portfolio Update");
			emapper.mapPortfolio(portfolio);
			
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

	
	private void setCMDParameters(CommandLine cmd) throws Exception {
		
		if (!cmd.hasOption('o')) throw new ParseException("Output argument not present");
		if (!cmd.hasOption('u')) throw new ParseException("Username argument not present");
		if (!cmd.hasOption('p')) throw new ParseException("Password argument not present");
		if (!cmd.hasOption('x')) throw new ParseException("Pin argument not present");
		
		this.username = cmd.getOptionValue('u');
		this.password = cmd.getOptionValue('p');
		this.pin = cmd.getOptionValue('x');
		
		String outfileStr = cmd.getOptionValue('o');
		if (outfileStr == null || "".equals(outfileStr)) throw new Exception("Invalid Output Dir");
		File outdirparent = new File(outfileStr);
		if (!outdirparent.exists() || !outdirparent.isDirectory()) throw new Exception("Output Directory Does not Exists");
		
		this.email = cmd.getOptionValue('e');
		if (email != null && !validEmail(email)) throw new Exception("Invalid email provided");
		
		this.outdir = new File(outdirparent, dateprefix.format(new Date()));
		if (!this.outdir.exists()) this.outdir.mkdir();

	}
	
	private CommandLine setAndParseOptions(String[] args) throws ParseException {
		options.addOption("u", "username", 	true, 	"Zerodha Coin Username");
		options.addOption("p", "password", 	true, 	"Zerodha Coin Password");
		options.addOption("x", "pin", 		true, 	"Zerodha Coin Pin");
		
		options.addOption("o", "outdir", 	true, 	"Output directory where portfolio files will be stored");
		options.addOption("e", "outdir", 	true, 	"Email to send the portfolio details");
		options.addOption("h", "help", 		false, 	"Help");		
		
		CommandLineParser 	parser 	= new DefaultParser();
		CommandLine 		cmd 	= parser.parse( options, args);
		
		return cmd;

	}
	
	private void printHelp() {
	     HelpFormatter formatter = new HelpFormatter();
	      formatter.printHelp("FileNameSorter", options);
	}
	
	private boolean validEmail(String email) {
		String emailRegex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

}
