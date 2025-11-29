package br.com.sistema.emailservice.infra.ses;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

/**
 * Classe de configuração do cliente Amazon SES (Simple Email Service).
 * 
 * Esta classe é responsável por:
 * - Carregar as credenciais AWS do arquivo application.properties
 * - Configurar e criar o cliente do Amazon SES
 * - Disponibilizar o cliente como um Bean gerenciado pelo Spring
 * 
 * O cliente configurado aqui será injetado automaticamente em outras classes
 * que precisarem se comunicar com o AWS SES (ex: SesEmailSender).
 * 
 * IMPORTANTE - Segurança:
 * Em produção, evite usar credenciais estáticas. Prefira:
 * - IAM Roles para aplicações EC2/ECS
 * - AWS Secrets Manager
 * - Variáveis de ambiente
 * 
 * @author Cesar Augusto
 * @version 1.0
 */
@Configuration
public class AwsSesConfig {

	/**
	 * Access Key ID da conta AWS.
	 * Carregada do application.properties através da anotação @Value.
	 * 
	 * Exemplo no application.properties:
	 * aws.accessKeyId=AKIAIOSFODNN7EXAMPLE
	 */
	@Value("${aws.accessKeyId}")
    private String accessKey;

	/**
	 * Secret Access Key da conta AWS.
	 * Carregada do application.properties através da anotação @Value.
	 * 
	 * Exemplo no application.properties:
	 * aws.secretKey=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
	 * 
	 * ATENÇÃO: Nunca commite esta chave no controle de versão!
	 */
    @Value("${aws.secretKey}")
    private String secretKey;

	/**
	 * Região da AWS onde o serviço SES será utilizado.
	 * Carregada do application.properties através da anotação @Value.
	 * 
	 * Exemplos de regiões:
	 * - sa-east-1 (São Paulo)
	 * - us-east-1 (Norte da Virgínia)
	 * - eu-west-1 (Irlanda)
	 * 
	 * Nota: Certifique-se de que o SES está disponível na região escolhida.
	 */
    @Value("${aws.region}")
    private String region;
	
	/**
	 * Cria e configura o cliente do Amazon Simple Email Service (SES).
	 * 
	 * Este método é anotado com @Bean, o que significa que o Spring irá:
	 * 1. Executar este método durante a inicialização da aplicação
	 * 2. Armazenar o objeto retornado no contexto do Spring
	 * 3. Disponibilizar este objeto para injeção em outras classes
	 * 
	 * Processo de configuração:
	 * 1. Cria um objeto BasicAWSCredentials com access key e secret key
	 * 2. Encapsula as credenciais em um AWSStaticCredentialsProvider
	 * 3. Usa o builder para construir o cliente com credenciais e região
	 * 4. Retorna o cliente configurado e pronto para uso
	 * 
	 * @return Cliente configurado do Amazon SES pronto para enviar emails
	 */
	@Bean
	public AmazonSimpleEmailService amazonSimpleEmailService() {
		// Cria objeto de credenciais básicas da AWS usando access key e secret key
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		
		// Constrói e retorna o cliente SES usando o padrão Builder
		return AmazonSimpleEmailServiceClientBuilder.standard()
				// Define o provedor de credenciais (autenticação na AWS)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            // Define a região AWS onde o SES será utilizado
	            .withRegion(region)
	            // Constrói e retorna o cliente configurado
	            .build();
	}
	
}