package com.dionlan.minhasfinancas.domain.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioDTO {

	private Long id;
	private String email;
	private String nome;
	
	@JsonIgnore
	private String senha;
	
	private String token;
}
