package com.dionlan.minhasfinancas.domain.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.exception.ErroAutenticacao;
import com.dionlan.minhasfinancas.domain.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.domain.exception.UsuarioNaoEncontradoException;
import com.dionlan.minhasfinancas.domain.repository.UsuarioRepository;
import com.dionlan.minhasfinancas.domain.service.UsuarioService;
/**
 * Implementação das regras de negócio.
 * 1. Cadastrar um usuário por e-mail, caso tente cadastrar com um email já cadastrado, é lançada a exceção RegraNegocioException. Só é possível cadastrar um usuário caso o seu email não esteja já cadastrado. 
 * @author Dionlan
 *
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository; //classe UsuarioService injetando a dependencia (ID) para UsuarioRepository @Autowired 
	
	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o e-mail informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha incorreta.");
		}
		
		return usuario.get();
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return usuarioRepository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		
		boolean existe = usuarioRepository.existsByEmail(email);
		
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse e-mail.");
		}
	}
	
	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return usuarioRepository.findById(id);

	}
	
	@Override
	public Usuario buscarOuFalhar(Long id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
	}
}
