package com.dionlan.minhasfinancas.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dionlan.minhasfinancas.domain.entity.Lancamento;
import com.dionlan.minhasfinancas.domain.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	@Query(value = " select sum(l.valor) from Lancamento l join l.usuario u "
				+ "  where u.id = :idUsuario and l.tipo = :tipo group by u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo);

	List<Lancamento> findByUsuario(Long id);
}
