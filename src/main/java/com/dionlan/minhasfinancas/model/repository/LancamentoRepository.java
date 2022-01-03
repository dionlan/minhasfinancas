package com.dionlan.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dionlan.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}