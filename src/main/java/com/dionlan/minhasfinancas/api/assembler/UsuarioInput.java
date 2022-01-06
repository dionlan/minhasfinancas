package com.dionlan.minhasfinancas.api.assembler;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioInput {
	
	@NotNull
	private String email;
	
	@NotNull
	private String senha;

}
