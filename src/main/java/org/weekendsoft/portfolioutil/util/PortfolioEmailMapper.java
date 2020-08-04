package org.weekendsoft.portfolioutil.util;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.weekendsoft.portfolioutil.model.PortfolioEntry;
import org.weekendsoft.portfolioutil.util.email.EmailSender;
import org.weekendsoft.portfolioutil.util.email.SendGridEmailSender;

public class PortfolioEmailMapper implements PortfolioMapper {
	
	private static final Logger LOG = Logger.getLogger(PortfolioEmailMapper.class);
	private static final DecimalFormat format = new DecimalFormat("#.####");
	
	private static String from = "portfolio@weekendsoft.org";
	private static String key = "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";
	
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
		EmailSender sg = new SendGridEmailSender(key);
		sg.sendPlainHTMLEmail(from, email, subject, html);
		LOG.debug("Email sent");
	}
	
	//TODO Use string templates to have a html template mail merge
	private String createPortfolioHTML(List<PortfolioEntry> list) {
		StringBuffer buf = new StringBuffer();
		
		buf.append("<html>\n" + 
				"	<body>\n" + 
				"		<table border=\"1\">\n");
		
		buf.append("			<tr>\n" + 
				"				<td>Symbol</td>\n" + 
				"				<td>Name</td>\n" + 
				"				<td>Cost Price</td>\n" + 
				"				<td>Price</td>\n" + 
				"				<td>Quantity</td>\n" + 
				"				<td>Cost basis</td>\n" + 
				"				<td>Total</td>\n" + 
				"				<td>Gain</td>\n" + 
				"				<td>% Gain</td>\n" + 
				"				<td>Comments</td>\n" + 
				"			</tr>\n");
		
		for (PortfolioEntry entry : list) {
		
			buf.append("			<tr>\n" + 
					"				<td>" + entry.getSymbol() + "</td>\n" + 
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
		
		buf.append("		</table>\n" + 
				"	</body>\n" + 
				"</html>");
		return buf.toString();
	}
	
	public static String formatDouble(double num) {
		return format.format(num);
	}
	
}
