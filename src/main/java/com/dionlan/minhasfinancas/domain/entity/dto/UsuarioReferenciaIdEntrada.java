package com.dionlan.minhasfinancas.domain.entity.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioReferenciaIdEntrada {
	
	@NotNull
	private Long id;
}
