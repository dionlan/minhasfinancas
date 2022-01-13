package com.dionlan.minhasfinancas.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.exception.EntidadeEmUsoException;
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
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public Usuario autenticar(String email, String senha) {
		Usuario usuario = usuarioRepository.findByEmail(email);
		
		if(usuario == null) {
			throw new ErroAutenticacao("Usuário não encontrado para o e-mail informado.");
		}
		
		boolean senhasBatem = encoder.matches(senha, usuario.getSenha());
		
		if(!senhasBatem) {
			throw new ErroAutenticacao("Senha incorreta.");
		}
		
		return usuario;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		criptografarSenha(usuario);
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
	public Usuario buscarOuFalhar(Long id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
	}
	
	@Override
	@Transactional
	public void deletar(Long id) {
		try {
			usuarioRepository.deleteById(id);
			usuarioRepository.flush();
			
		} catch (EmptyResultDataAccessException e ) {
			throw new UsuarioNaoEncontradoException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Usuário de código %d não pode ser removido pois está em uso.", id));
		}
	}
	
	private void criptografarSenha(Usuario usuario) {
		String senha = usuario.getSenha();
		String senhaCriptografada = encoder.encode(senha);
		usuario.setSenha(senhaCriptografada);
	}
}
