package com.dionlan.minhasfinancas.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dionlan.minhasfinancas.domain.entity.Lancamento;

@Component
public class LancamentoEntradaDTODisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * Conversão da classe de Input (entrada dos dados) para um objeto de domínio lançamento
	 */
	public Lancamento converteDtoParaEntidade(LancamentoInput lancamentoInput) {
		
		return modelMapper.map(lancamentoInput, Lancamento.class);
	}
	
	public void converteDtoParaEntidadeParaAtualizacao(LancamentoInput lancamentoInput, Lancamento lancamento) {
		/*no momento da atualização, que o id da cozinha atualizada sera atribuida ao id da cozinha ja existente,
		*ou seja, permitir que seja alterada a cozinha para determinado restaurante */
		//lancamento.setUsuario(new Usuario());
		modelMapper.map(lancamentoInput, lancamento);
	}
}
