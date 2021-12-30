package com.dionlan.minhasfinancas.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	MENSAGEM_INCOMPREENSIVEL ("/mensagem-incompreensivel", "Mensagem Incompreessível"),
	
	ENTIDADE_NAO_ENCONTRADA ("/entidade-nao-encontrada", "Entidade não encontrada"),
	
	ENTIDADE_EM_USO ("/entidade-em-uso", "Entidade está em uso"), 
	
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso de URL não encontrado"),
	
	ERRO_SISTEMA("/erro-sistemo", "Erro interno no sistema"),
	
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos");
	
	private String title;
	
	private String uri;
	
	 private ProblemType(String path, String title) {
		 this.uri = "https://dionlan-algafood.com.br" + path;
		 this.title = title;
	 }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
