package com.dionlan.minhasfinancas.domain.entity.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioReferenciaIdEntrada {
	
	@NotNull
	@Column(name="id")
	private Long userId;
}
