package com.dionlan.minhasfinancas.domain.service;

import java.util.Optional;

import com.dionlan.minhasfinancas.domain.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
	Optional<Usuario> obterPorId(Long id);
	
	Usuario buscarOuFalhar(Long id);
}
