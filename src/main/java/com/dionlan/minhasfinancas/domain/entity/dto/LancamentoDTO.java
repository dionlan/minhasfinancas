package com.dionlan.minhasfinancas.domain.entity.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LancamentoDTO {

	private Long id;
	private String tipo;
	private String descricao;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	private UsuarioDTO usuario;
	private String status;
}
