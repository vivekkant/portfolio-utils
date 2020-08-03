package org.weekendsoft.portfolioutil.util.email;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class SendGridEmailSender implements EmailSender {
	
	public SendGrid sg;
	
	
	public SendGridEmailSender(String key) {
		this.sg = new SendGrid(key);
	}

	@Override
	public void sendPlainTextEmail(String from, String to, String subject, String body) throws Exception {
		sendEmail(from, to, subject, body, "text/plain");
	}

	@Override
	public void sendPlainHTMLEmail(String from, String to, String subject, String body) throws Exception {
		sendEmail(from, to, subject, body, "text/html");
	}
	
	private void sendEmail(String from, String to, String subject, String body, String contentType) throws Exception {
		 
		Email fromEmail = new Email(from);
		Email toEmail = new Email(to);
		Content content = new Content(contentType, body);
		Mail mail = new Mail(fromEmail, subject, toEmail, content);
		
		Request request = new Request();
		
		request.setMethod(Method.POST);
		request.setEndpoint("mail/send");
		request.setBody(mail.build());
		
		Response response = sg.api(request);
		int status = response.getStatusCode();
		
		if (status != 200 && status != 201 && status != 202) {
			throw new Exception("Error status received : " + status);
		}
		
	}
	
	
}
