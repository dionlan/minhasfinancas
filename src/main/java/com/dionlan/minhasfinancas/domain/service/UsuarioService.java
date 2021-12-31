package com.dionlan.minhasfinancas.domain.service;

import com.dionlan.minhasfinancas.domain.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
	Usuario buscarOuFalhar(Long id);
	
	void deletar(Long id);
}
