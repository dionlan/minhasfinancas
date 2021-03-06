package com.dionlan.minhasfinancas.domain.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dionlan.minhasfinancas.domain.entity.Lancamento;
import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.enums.StatusLancamento;
import com.dionlan.minhasfinancas.domain.enums.TipoLancamento;
import com.dionlan.minhasfinancas.domain.exception.EntidadeEmUsoException;
import com.dionlan.minhasfinancas.domain.exception.LancamentoNaoEncontradoException;
import com.dionlan.minhasfinancas.domain.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.domain.repository.LancamentoRepository;
import com.dionlan.minhasfinancas.domain.service.LancamentoService;
import com.dionlan.minhasfinancas.domain.service.UsuarioService;

@Service
public class LancamentoServiceImpl implements LancamentoService{

	@Autowired
	private LancamentoRepository repository;
	
	@Autowired(required = true)
	private UsuarioService usuarioService;
	
	@Override
	public List<Lancamento> listar(){
		return repository.findAll();
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validarLancamento(lancamento);
		
		Long id = lancamento.getUsuario().getUserId();
		Usuario usuario = usuarioService.buscarOuFalhar(id);
		
		lancamento.setUsuario(usuario);
		
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validarLancamento(lancamento);
		return repository.save(lancamento);
	}
	
	@Override
	@Transactional
	public Lancamento obterPorId(Long id){
		return repository.findById(id).orElseThrow(() -> new LancamentoNaoEncontradoException(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		try {
			Example<Lancamento> example = Example.of(lancamentoFiltro, ExampleMatcher
					.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING));
			
			
			return repository.findAll(example);
		}catch(LancamentoNaoEncontradoException e) {
			throw new LancamentoNaoEncontradoException(e.getMessage());
		}
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> findByIdUsuario(Long id_usuario) {
		
		return repository.findByUsuario(id_usuario);
	}
	

	@Override
	@Transactional
	public void deletar(Long id) {
		try {
			repository.deleteById(id);
			repository.flush();
			
		} catch (EmptyResultDataAccessException e ) {
			throw new LancamentoNaoEncontradoException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Lan??amento de c??digo %d n??o pode ser removido pois est?? em uso.", id));

		}

	}

	@Override
	@Transactional
	public void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento) {
		lancamento.setStatus(statusLancamento);
		atualizar(lancamento);
	}
	
	@Override
	public void validarLancamento(Lancamento lancamento) {
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informa uma descri????o v??lida.");
		}
		
		if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um m??s v??lido.");
		}
		
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um ano v??lido.");
		}
		
		if(lancamento.getUsuario() == null || lancamento.getUsuario().getUserId() == null) {
			throw new RegraNegocioException("Informe um usu??rio.");
		}
		
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um valor v??lido.");
		}
		
		if(lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de lan??amento.");
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoPorUsuario(Long id) {
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO);
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO);
		
		if(receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		
		if(despesas == null) {
			despesas = BigDecimal.ZERO;
		}
		return receitas.subtract(despesas);
	}


}
