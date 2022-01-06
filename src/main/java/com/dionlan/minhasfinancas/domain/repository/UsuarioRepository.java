package com.dionlan.minhasfinancas.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dionlan.minhasfinancas.domain.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	boolean existsByEmail(String email);
	
	Usuario findByEmail(String email);
	
	//Optional<Usuario> findByEmail(String email);
	
	Optional<Usuario> findById(Long id);
	
}
