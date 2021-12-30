package com.dionlan.minhasfinancas.domain.exception;

/*
 * Exception mais genérica para tratar qualquer erro da camada de negócio (Serviço) que não seja específico
 */

public class NegocioException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NegocioException(String mensagem) {
		super(mensagem);
	}

	public NegocioException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
