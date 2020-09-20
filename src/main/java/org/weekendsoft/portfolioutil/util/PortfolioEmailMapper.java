package org.weekendsoft.portfolioutil.util;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.weekendsoft.emailsender.EmailSender;
import org.weekendsoft.emailsender.SendInBlueEmailSender;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;

public class PortfolioEmailMapper implements PortfolioMapper {
	
	private static final Logger LOG = Logger.getLogger(PortfolioEmailMapper.class);
	private static final DecimalFormat format = new DecimalFormat("#.##");
	
	private static String from = "portfolio@weekendsoft.org";
	
	private String email;
	private String subject;
	
	public PortfolioEmailMapper(String email, String subject) {
		this.email = email;
		this.subject = subject;
		LOG.debug("Email to send is " + email);
		LOG.debug("Subject to send is " + subject);
	}

	@Override
	public void mapPortfolio(List<PortfolioEntry> list) throws Exception {
		
		String html = createPortfolioHTML(list);
		LOG.debug("HTML to send : " + html);

		LOG.debug("Sending email to " + email);
		EmailSender sg = SendInBlueEmailSender.getInstance();
		sg.sendHTMLEmail(from, email, subject, html);
		LOG.debug("Email sent");
	}
	
	//TODO Use string templates to have a html template mail merge
	private String createPortfolioHTML(List<PortfolioEntry> list) {
		StringBuffer buf = new StringBuffer();
		
		buf.append("<html lang=\"en\">\n" + 
				"" + 
				"<head>" + 
				"  <meta charset=\"utf-8\">" + 
				"  <title>Portfolio Update</title>" + 
				"</head>" + 
				"\n" + 
				"<body>" + 
				"\n" + 
				"<table border=\"1px\">" + 
				"<thead>" + 
				"  <tr>" + 
				"  <tr>" + 
				"    <th width=\"180\">Symbol</th>" + 
				"    <th width=\"120\">Name</th>" + 
				"    <th width=\"100\">Cost Price</th>" + 
				"    <th width=\"100\">Price</th>" + 
				"    <th width=\"100\">Quantity</th>" + 
				"    <th width=\"100\">Cost Basis</th>" + 
				"    <th width=\"100\">Total</th>" + 
				"    <th width=\"100\">Gain</th>" + 
				"    <th width=\"100\">Gain %</th>" + 
				"    <th width=\"180\">Comments</th>" + 
				"  </tr>" + 
				"</thead>" + 
				"<tbody>");
		
		
		for (PortfolioEntry entry : list) {
			
		 buf.append(" " +
		 		"<tr>" + 
		 		"    <td>" + entry.getSymbol() + "</td>" + 
		 		"    <td>" + entry.getName() + "</td>" + 
		 		"    <td align=\"right\">" + formatDouble(entry.getCostPrice()) + "</td>" + 
		 		"    <td align=\"right\">" + formatDouble(entry.getPrice()) + "</td>" + 
		 		"    <td align=\"right\">" + formatDouble(entry.getQuantity()) + "</td>" + 
		 		"    <td align=\"right\">" + formatDouble(entry.getCostBasis()) + "</td>" + 
		 		"    <td align=\"right\">" + formatDouble(entry.getTotal()) + "</td>" + 
		 		"    <td align=\"right\">" + formatDouble(entry.getGain()) + "</td>" + 
		 		"    <td align=\"right\">" + formatDouble(entry.getGainPercentage()) + "</td>" + 
		 		"    <td>" + (entry.getComments() == null ? "" : entry.getComments()) + "</td>" + 
		 		"  </tr>");
				
			}
		
		buf.append("    </tbody>" + 
				"  </table>" + 
				"" + 
				"</body>" + 
				"\n" + 
				"</html>");
		return buf.toString();
	}
	
	public static String formatDouble(double num) {
		return format.format(num);
	}
	
}
