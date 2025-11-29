package br.com.sistema.emailservice.core;

public record EmailRequest(String to, String subject, String body) {

}
