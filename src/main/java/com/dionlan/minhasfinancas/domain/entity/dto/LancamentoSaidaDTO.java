package com.dionlan.minhasfinancas.domain.entity.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dionlan.minhasfinancas.domain.entity.Lancamento;

/**
 * Classe montadora de retaurante model converter a dto para entity
 * @author Dionlan
 *
 */
@Component
public class LancamentoSaidaDTO {
	
	@Autowired
	private ModelMapper modelMapper; //mapeia o que está recebendo de entrada - restaurante - para um objeto destino - RestauranteModel.class
	
	public LancamentoDTO converteParaDto(Lancamento lancamento) {
		/*modelMapper.typeMap(LancamentoDTO.class,Lancamento.class).addMappings(mapper -> {
	        mapper.skip(Lancamento::setDescricao);
	    });*/
		return modelMapper.map(lancamento, LancamentoDTO.class);
	}
	
	public List<LancamentoDTO> converteParaColecaode(List<Lancamento> lancamentos){
		return lancamentos.stream()
				.map(lancamento -> converteParaDto(lancamento))
				.collect(Collectors.toList());
	}
	
}
