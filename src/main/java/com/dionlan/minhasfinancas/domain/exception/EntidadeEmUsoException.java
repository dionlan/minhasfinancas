package com.dionlan.minhasfinancas.domain.exception;

public class EntidadeEmUsoException extends RegraNegocioException {

	private static final long serialVersionUID = 1L;

	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}
}
