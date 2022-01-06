package com.dionlan.minhasfinancas.domain.entity.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dionlan.minhasfinancas.domain.entity.Usuario;

/**
 * Classe montadora de retaurante model converter a dto para entity
 * @author Dionlan
 *
 */
@Component
public class UsuarioSaidaDTO {
	
	@Autowired
	private ModelMapper modelMapper; //mapeia o que está recebendo de entrada - Usuario - para um objeto destino - UsuarioDTO.class
	
	public UsuarioDTO converteParaDto(Usuario usuario) {
		return modelMapper.map(usuario, UsuarioDTO.class);
	}
	
	public List<UsuarioDTO> converteParaColecaoDeDTO(List<Usuario> usuarios){
		return usuarios.stream()
				.map(usuario -> converteParaDto(usuario))
				.collect(Collectors.toList());
	}
	
}
