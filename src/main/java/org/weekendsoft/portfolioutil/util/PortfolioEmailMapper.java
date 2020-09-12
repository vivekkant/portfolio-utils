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
		
		buf.append("<!DOCTYPE html>\n" + 
				"<html lang=\"en\">\n" + 
				"\n" + 
				"<head>\n" + 
				"  <meta charset=\"utf-8\">\n" + 
				"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" + 
				"  <meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n" + 
				"  <title>Material Design Bootstrap</title>\n" + 
				"  <!-- Font Awesome -->\n" + 
				"  <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.8.2/css/all.css\">\n" + 
				"  <!-- Bootstrap core CSS -->\n" + 
				"  <link href=\"https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/css/bootstrap.min.css\" rel=\"stylesheet\">\n" + 
				"  <!-- Material Design Bootstrap -->\n" + 
				"  <link href=\"https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.1/css/mdb.min.css\" rel=\"stylesheet\">\n" + 
				"</head>\n" + 
				"\n" + 
				"<body>\n" + 
				"\n" + 
				"  <!-- Start your project here-->\n" + 
				"  <table class=\"table table-bordered\">\n" + 
				"    <thead>\n" + 
				"      <tr>\n" + 
				"        <th scope=\"col\">Symbol</th>\n" + 
				"        <th scope=\"col\">Name</th>\n" + 
				"        <th scope=\"col\">Cost Price</th>\n" + 
				"        <th scope=\"col\">Price</th>\n" + 
				"        <th scope=\"col\">Quantity</th>\n" + 
				"        <th scope=\"col\">Cost Basis</th>\n" + 
				"        <th scope=\"col\">Total</th>\n" + 
				"        <th scope=\"col\">Gain</th>\n" + 
				"        <th scope=\"col\">Gain %</th>\n" + 
				"        <th scope=\"col\">Comments</th>\n" + 
				"      </tr>\n" + 
				"    </thead>\n" + 
				"    <tbody>");
		
		
		for (PortfolioEntry entry : list) {
		
			buf.append("			<tr>\n" + 
					"				<th scope=\"row\">" + entry.getSymbol() + "</th>\n" + 
					"				<td>" + entry.getName() + "</td>\n" + 
					"				<td>" + formatDouble(entry.getCostPrice()) + "</td>\n" + 
					"				<td>" + formatDouble(entry.getPrice()) + "</td>\n" + 
					"				<td>" + formatDouble(entry.getQuantity()) + "</td>\n" + 
					"				<td>" + formatDouble(entry.getCostBasis()) + "</td>\n" + 
					"				<td>" + formatDouble(entry.getTotal()) + "</td>\n" + 
					"				<td>" + formatDouble(entry.getGain()) + "</td>\n" + 
					"				<td>" + formatDouble(entry.getGainPercentage()) + "</td>\n" + 
					"				<td>" + (entry.getComments() == null ? "" : entry.getComments()) + "</td>\n" + 
					"			</tr>\n");		
			}
		
		buf.append("    </tbody>\n" + 
				"  </table>\n" + 
				"  <!-- /Start your project here-->\n" + 
				"\n" + 
				"  <!-- SCRIPTS -->\n" + 
				"<!-- JQuery -->\n" + 
				"<script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>\n" + 
				"<!-- Bootstrap tooltips -->\n" + 
				"<script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.4/umd/popper.min.js\"></script>\n" + 
				"<!-- Bootstrap core JavaScript -->\n" + 
				"<script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/js/bootstrap.min.js\"></script>\n" + 
				"<!-- MDB core JavaScript -->\n" + 
				"<script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.1/js/mdb.min.js\"></script>\n" + 
				"</body>\n" + 
				"\n" + 
				"</html>");
		return buf.toString();
	}
	
	public static String formatDouble(double num) {
		return format.format(num);
	}
	
}
