package com.dionlan.minhasfinancas.model.repository;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dionlan.minhasfinancas.model.entity.Usuario;

/**
 * Testes de integração. Acessa recursos fora da aplicação, como o repositório do banco de dados. Diferente do teste unitário;
 * @author Dionlan
 *
 */
@SpringBootTest
public class UsuarioServiceRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = Usuario.builder()
				.nome("Dionlan Alves de Jesus")
				.email("dionlan.alves@gmail.com")
				.senha("dionlan")
				.dataCadastro(LocalDateTime.now())
				.build();
		
		usuarioRepository.save(usuario);
		
		//ação / execução
		boolean resultado = usuarioRepository.existsByEmail("dionlan.alves@gmail.com");
		
		//verificação
		Assertions.assertThat(resultado).isTrue();
		
	}

}
