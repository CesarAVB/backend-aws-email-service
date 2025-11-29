package br.com.sistema.emailservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import br.com.sistema.emailservice.application.EmailSenderService;
import br.com.sistema.emailservice.core.EmailRequest;
import br.com.sistema.emailservice.core.exeptions.EmailServiceException;

@Controller
@RequestMapping("/api/email")
public class EmailSenderController {

	private final EmailSenderService emailSenderService;

	@Autowired
	public EmailSenderController(EmailSenderService emailService) {
		this.emailSenderService = emailService;
	}
	
	
	@PostMapping()
	public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
		try {
			this.emailSenderService.sendEmail(request.to(), request.subject(), request.body());
			return ResponseEntity.ok("Email enviado com sucesso!");
			
		} catch (EmailServiceException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro enquanto enviava o email.");
		}
	}
	
	
}
