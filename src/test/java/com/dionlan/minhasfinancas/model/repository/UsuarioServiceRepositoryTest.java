package com.dionlan.minhasfinancas.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
@DataJpaTest //cria uma transação na base de dados, executa o teste e após faz o rollback do estado antes de cada teste, pode-se retirar o método deleteAll();
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioServiceRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		
		entityManager.persist(usuario);
		
		//ação / execução
		boolean resultado = usuarioRepository.existsByEmail("dionlan.alves@gmail.com");
		
		//verificação
		assertThat(resultado).isTrue();
		
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		//cenário
		
		//ação / execução
		boolean resultado = usuarioRepository.existsByEmail("dionlan.alves@gmail.com");
		
		//verificação
		assertThat(resultado).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario = criarUsuario();
		
		//ação / execução
		usuario = usuarioRepository.save(usuario);
		
		//verificação
		assertThat(usuario.getId()).isNotNull();
	}
	
	@Test
	public void deveRetornarUsuarioEncontradoPorEmail_QuandoExistirNaBase() {
		//cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//verificação
		Optional<Usuario> resultado = usuarioRepository.findByEmail("dionlan.alves@gmail.com");
		
		assertThat(resultado.isPresent()).isTrue();	
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmail_QuandoNaoExistirNaBase() {

		//verificação
		Optional<Usuario> resultado = usuarioRepository.findByEmail("dionlan.alves@gmail.com");
		
		assertThat(resultado.isPresent()).isFalse();	
	}
	
	public static Usuario criarUsuario() {
		return Usuario
				.builder()
				.nome("Dionlan Alves de Jesus")
				.email("dionlan.alves@gmail.com")
				.senha("dionlan")
				.dataCadastro(LocalDateTime.now())
				.build();
	}

}
