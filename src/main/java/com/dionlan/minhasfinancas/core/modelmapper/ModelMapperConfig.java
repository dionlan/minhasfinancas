package com.dionlan.minhasfinancas.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dionlan.minhasfinancas.domain.entity.Lancamento;
import com.dionlan.minhasfinancas.domain.entity.dto.LancamentoDTO;


@Configuration //instancia de modelmapper dentro do contexto do spring. Auxilia no mapeamento do Medelo para Entidate Java
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper ModelMapper() {
		var modelMapper = new ModelMapper();
		
		modelMapper.createTypeMap(Lancamento.class, LancamentoDTO.class);
		
		return modelMapper;
	}
}
