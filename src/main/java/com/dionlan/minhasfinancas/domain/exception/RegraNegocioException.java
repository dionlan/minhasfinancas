package com.dionlan.minhasfinancas.domain.exception;

/*
 * Exception mais genérica para tratar qualquer erro da camada de negócio (Serviço) que não seja específico
 */

public class RegraNegocioException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RegraNegocioException(String mensagem) {
		super(mensagem);
	}

	public RegraNegocioException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
