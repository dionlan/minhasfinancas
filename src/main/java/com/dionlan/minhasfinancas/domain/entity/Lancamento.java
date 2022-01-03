package com.dionlan.minhasfinancas.domain.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.CreationTimestamp;

import com.dionlan.minhasfinancas.domain.enums.StatusLancamento;
import com.dionlan.minhasfinancas.domain.enums.TipoLancamento;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Lancamento {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descricao;
	private Integer ano;
	private Integer mes;
	
	@NotNull
	@PositiveOrZero
	private BigDecimal valor;
	
	@Valid
	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "status_lancamento")
	private StatusLancamento status;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "tipo_lancamento")
	private TipoLancamento tipo;
	
	@CreationTimestamp
	@Column(name = "data_cadastro", nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;
	
}
