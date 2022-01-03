package com.dionlan.minhasfinancas.domain.exception;

public class LancamentoNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public LancamentoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public LancamentoNaoEncontradoException(Long id) {
		this(String.format("Não existe cadastro para o lançamento com código %d ", id));
	}
}
