package org.weekendsoft.portfolioutil.util.email;

import org.junit.jupiter.api.Test;

class SendGridEmailSenderTest {

	@Test
	void test() throws Exception {
		
		EmailSender es = new SendGridEmailSender("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");
		
		String from = "portfolio@weekendsoft.org";
		String to = "vivek.kant@gmail.com";
		String subject = "Test Email";
		String body = "It is fun to send emails";
		
		es.sendPlainHTMLEmail(from, to, subject, body);
		
	}

}
