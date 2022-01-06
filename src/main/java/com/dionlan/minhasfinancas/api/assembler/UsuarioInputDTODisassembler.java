package com.dionlan.minhasfinancas.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dionlan.minhasfinancas.domain.entity.Usuario;

@Component
public class UsuarioInputDTODisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * Conversão da classe de Input (entrada dos dados) para um objeto de domínio lançamento
	 */
	public Usuario converteDtoParaEntidade(UsuarioInput usuarioInput) {
		
		return modelMapper.map(usuarioInput, Usuario.class);
	}
	
	public void converteDtoParaEntidadeParaAtualizacao(UsuarioInput usuarioInput, Usuario usuario) {
		modelMapper.map(usuarioInput, usuario);
	}
}
