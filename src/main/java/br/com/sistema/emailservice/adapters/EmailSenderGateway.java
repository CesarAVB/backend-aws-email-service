package br.com.sistema.emailservice.adapters;

public interface EmailSenderGateway {
	
	void sendEmail(String to, String subject, String body);

}
