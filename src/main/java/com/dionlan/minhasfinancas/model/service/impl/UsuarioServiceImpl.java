package com.dionlan.minhasfinancas.model.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dionlan.minhasfinancas.model.entity.Usuario;
import com.dionlan.minhasfinancas.model.exception.ErroAutenticacao;
import com.dionlan.minhasfinancas.model.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.model.service.UsuarioService;
import com.dionlan.minhasfinancas.repository.UsuarioRepository;
/**
 * Implementação das regras de negócio.
 * 1. Cadastrar um usuário por e-mail, caso tente cadastrar com um email já cadastrado, é lançada a exceção RegraNegocioException. Só é possível cadastrar um usuário caso o seu email não esteja já cadastrado. 
 * @author Dionlan
 *
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository usuarioRepository; //classe UsuarioService injetando a dependencia (ID) para UsuarioRepository @Autowired 
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}
	
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

}
