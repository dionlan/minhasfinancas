package com.dionlan.minhasfinancas.api.assembler;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.dionlan.minhasfinancas.domain.entity.dto.UsuarioReferenciaIdEntrada;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LancamentoInput {
	
	@NotNull
	private String tipo;
	
	@NotNull
	private String descricao;
	
	@NotNull
	@PositiveOrZero 
	private Integer mes;
	
	@NotNull
	@PositiveOrZero 
	private Integer ano;
	
	@NotNull
	@PositiveOrZero 
	private BigDecimal valor;
	
	@NotNull
	private String status;
	
	@Valid
	@NotNull
	private UsuarioReferenciaIdEntrada usuario;
	
}
