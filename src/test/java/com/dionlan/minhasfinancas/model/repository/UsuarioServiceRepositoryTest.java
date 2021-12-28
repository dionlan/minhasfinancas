package com.dionlan.minhasfinancas.model.repository;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dionlan.minhasfinancas.model.entity.Usuario;

/**
 * Testes de integração. Acessa recursos fora da aplicação, como o repositório do banco de dados. Diferente do teste unitário;
 * @author Dionlan
 * A partir de out/nov de 2019 a versão 2.2 do Spring Boot implementa o JUnit 5 com as novas anotações
 * Susbstituir @RunWith(SpringRunner.class) para @ExtendWith(SpringExtension.class)
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
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
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		//cenário
		usuarioRepository.deleteAll();
		
		//ação / execução
		boolean resultado = usuarioRepository.existsByEmail("dionlan.alves@gmail.com");
		
		//verificação
		Assertions.assertThat(resultado).isFalse();
	}

}
