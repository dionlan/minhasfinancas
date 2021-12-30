package com.dionlan.minhasfinancas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dionlan.minhasfinancas.domain.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
