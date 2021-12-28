package com.dionlan.minhasfinancas.service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dionlan.minhasfinancas.model.entity.Usuario;
import com.dionlan.minhasfinancas.model.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.model.repository.UsuarioRepository;
import com.dionlan.minhasfinancas.model.service.UsuarioService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
public class UsuarioServiceTest {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Espera-se que não lance nenhuma exceção, pois nao haverá nenhum email cadastrado no teste
	 */
	@Test
	public void deveValidarEmail_QuandoNaoHouverOutroEmailIgualCadastrado() {
		//cenário
		usuarioRepository.deleteAll();
		
		//ação / execução
		usuarioService.validarEmail("dionlan.alves@gmail.com");
		
	}
	
	/**
	 * Espera-se que lance a exceção, pois haverá um email identico já cadastrado no teste
	 */
	@Test
	public void deveFalharValidacaoEmail_QuandoHouverOutroEmailIgualCadastrado() {
		//cenário
		Usuario usuario = Usuario.builder()
				.nome("Dionlan Alves de Jesus")
				.email("dionlan.alves@gmail.com")
				.senha("dionlan")
				.dataCadastro(LocalDateTime.now())
				.build();
		
		usuarioRepository.save(usuario);
		
		//ação / execução
		Assertions.assertThrows(RegraNegocioException.class, () -> {
		usuarioService.validarEmail("dionlan.alves@gmail.com");
		});
		
	}
	
}
