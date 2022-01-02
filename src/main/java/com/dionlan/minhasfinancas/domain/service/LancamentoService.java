package com.dionlan.minhasfinancas.domain.service;

import java.math.BigDecimal;
import java.util.List;

import com.dionlan.minhasfinancas.domain.entity.Lancamento;
import com.dionlan.minhasfinancas.domain.enums.StatusLancamento;

public interface LancamentoService {

	List<Lancamento> listar();
	Lancamento salvar(Lancamento lancamento);
	Lancamento atualizar(Lancamento lancamento);
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	void deletar(Long id);
	void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento);
	void validarLancamento(Lancamento lancamento);
	Lancamento obterPorId(Long id);
	BigDecimal obterSaldoPorUsuario(Long id);
	
}

