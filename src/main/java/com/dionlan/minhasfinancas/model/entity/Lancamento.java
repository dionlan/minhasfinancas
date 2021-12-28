package com.dionlan.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dionlan.minhasfinancas.model.enums.StatusLancamento;
import com.dionlan.minhasfinancas.model.enums.TipoLancamento;

import lombok.Data;

@Entity
@Data
public class Lancamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descricao;
	private Integer ano;
	private Integer mes;
	private BigDecimal valor;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Enumerated(value = EnumType.STRING)
	private StatusLancamento status;
	
	@Enumerated(value = EnumType.STRING)
	private TipoLancamento tipo;
	
	@Column(name = "data_cadastro")
	private LocalDateTime dataCadastro;
	
}
