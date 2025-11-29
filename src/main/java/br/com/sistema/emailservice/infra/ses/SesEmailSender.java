package br.com.sistema.emailservice.infra.ses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import br.com.sistema.emailservice.adapters.EmailSenderGateway;
import br.com.sistema.emailservice.core.exeptions.EmailServiceException;

/**
 * Implementação do gateway de envio de emails utilizando Amazon SES (Simple Email Service).
 * 
 * Esta classe é responsável pela comunicação direta com o serviço da AWS, 
 * convertendo os dados de email em objetos do SDK da Amazon e tratando 
 * possíveis exceções durante o envio.
 * 
 * Como implementa EmailSenderGateway, permite que a aplicação troque facilmente
 * o provedor de email sem impactar as camadas superiores (princípio da Clean Architecture).
 * 
 * @author Cesar Augusto
 * @version 1.0
 */
@Service
public class SesEmailSender implements EmailSenderGateway {

	/**
	 * Cliente do Amazon Simple Email Service fornecido pelo SDK da AWS.
	 * Este objeto é configurado no AwsSesConfig e injetado via construtor.
	 */
	private final AmazonSimpleEmailService amazonSimpleEmailService;
	
	/**
	 * Construtor com injeção de dependência do cliente AWS SES.
	 * 
	 * @param amazonSimpleEmailService Cliente configurado do Amazon SES
	 */
	@Autowired
	public SesEmailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
		this.amazonSimpleEmailService = amazonSimpleEmailService;
	}

	/**
	 * Envia um email através do Amazon SES.
	 * 
	 * Este método constrói a requisição de envio usando os objetos do SDK da AWS:
	 * - SendEmailRequest: objeto principal da requisição
	 * - Destination: define o(s) destinatário(s)
	 * - Message: contém assunto e corpo do email
	 * - Content: encapsula o texto do assunto e corpo
	 * - Body: permite definir o formato (texto simples ou HTML)
	 * 
	 * Observações importantes:
	 * 1. O email remetente (withSource) deve estar verificado no AWS SES
	 * 2. Se a conta estiver em sandbox, o destinatário também precisa estar verificado
	 * 3. Atualmente suporta apenas emails em texto simples (não HTML)
	 * 
	 * @param to Endereço de email do destinatário
	 * @param subject Assunto do email
	 * @param body Corpo do email em texto simples
	 * @throws EmailServiceException Se ocorrer erro na comunicação com AWS SES
	 */
	@Override
	public void sendEmail(String to, String subject, String body) {
		// Constrói a requisição de envio de email com padrão fluent (builder pattern)
		SendEmailRequest request = new SendEmailRequest()
				// Define o email remetente (deve estar verificado no AWS SES)
				.withSource("cesar.augusto.rj1@gmail.com")
				// Define o destinatário do email
				.withDestination(new Destination().withToAddresses(to))
				// Constrói a mensagem com assunto e corpo
				.withMessage(new Message()
						// Define o assunto do email
						.withSubject(new Content(subject))
						// Define o corpo como texto simples
						// Nota: Para HTML, use .withBody(new Body().withHtml(new Content(body)))
						.withBody(new Body().withText(new Content(body)))
				);
		
		try {
			// Envia o email através do cliente AWS SES
			this.amazonSimpleEmailService.sendEmail(request);
		} catch (AmazonServiceException exception) {
			// Captura exceções específicas da AWS (problemas de autenticação, limite de envio, etc.)
			// e converte para exceção do domínio da aplicação (Clean Architecture)
			throw new EmailServiceException("Erro enquanto enviava o email", exception);
		}
	}

}