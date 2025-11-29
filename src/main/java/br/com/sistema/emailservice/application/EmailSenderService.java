package br.com.sistema.emailservice.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.sistema.emailservice.adapters.EmailSenderGateway;
import br.com.sistema.emailservice.core.EmailSenderUseCase;

@Service
public class EmailSenderService implements EmailSenderUseCase {
	
	private final EmailSenderGateway emailSenderGateway;
	
	@Autowired
	public EmailSenderService(EmailSenderGateway emailGateway) {
		this.emailSenderGateway = emailGateway;
	}

	@Override
	public void sendEmail(String to, String subject, String body) {
		this.emailSenderGateway.sendEmail(to, subject, body);
	}

}
