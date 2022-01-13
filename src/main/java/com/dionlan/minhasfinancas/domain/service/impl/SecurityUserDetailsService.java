package com.dionlan.minhasfinancas.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.repository.UsuarioRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * pode carregar qualquer o usuário de qualquer fonte de dados. Ex.: planilha, outros bancos de dados
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		Usuario usuarioEncontrado = usuarioRepository.findByEmail(email);
		
		if(usuarioEncontrado == null) {
			throw new UsernameNotFoundException("Usuário não encontrado para o e-mail informado.");
		}
		
		return User.builder()
				.username(usuarioEncontrado.getEmail())
				.password(usuarioEncontrado.getSenha())
				.roles("USER")
				.build();
	}
}
