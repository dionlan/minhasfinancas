package com.dionlan.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dionlan.minhasfinancas.model.entity.Usuario;
import com.dionlan.minhasfinancas.model.exception.ErroAutenticacao;
import com.dionlan.minhasfinancas.model.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.model.repository.UsuarioRepository;
import com.dionlan.minhasfinancas.model.service.UsuarioService;
import com.dionlan.minhasfinancas.model.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
public class UsuarioServiceTest {

	private UsuarioService usuarioService;
	
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	@BeforeEach
	public void setup() {
		usuarioService = new UsuarioServiceImpl(usuarioRepository);
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		String email = "dionlan.alves@gmail.com";
		String senha = "dionlan";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
		
		Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//ação
		Usuario resultado = usuarioService.autenticar(email, senha);
		
		//verificação
		assertThat(resultado).isNotNull();
	}
	/**
	 * teste para cair na exceção ErroAutenticacao quando encontrar um usuario já cadastrado com o email informado
	 */
	@Test
	public void deveLancarErro_QuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		//cenário
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//ação / execução
		 Throwable exception = catchThrowable(() -> usuarioService.autenticar("dionlan.alves@gmail.com", "123"));
		 
		 assertThat(exception)
		 .isInstanceOf(ErroAutenticacao.class)
        .hasMessage("Usuário não encontrado para o e-mail informado.");
	}
	
	@Test
	public void deveLancarErro_QuandoSenhaNaoBater() {
		//cenário
		String senha = "dionlan";
		Usuario usuario = Usuario.builder().email("dionlan.alves@gmail.com").senha(senha).build();
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//ação / execução
		 Throwable exception = catchThrowable(() -> usuarioService.autenticar("dionlan.alves@gmail.com", "123"));
		 
		 assertThat(exception)
		 .isInstanceOf(ErroAutenticacao.class)
         .hasMessage("Senha incorreta.");
		
	}
	
	/**
	 * Espera-se que não lance nenhuma exceção, pois nao haverá nenhum email cadastrado no teste
	 */
	@Test
	public void deveValidarEmail_QuandoNaoHouverOutroEmailIgualCadastrado() {
		//cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação / execução
		usuarioService.validarEmail("dionlan.alves@gmail.com");
		
	}
	
	/**
	 * Espera-se que lance a exceção, pois haverá um email identico já cadastrado no teste
	 */
	@Test
	public void deveLancarErroAoValidarEmail_QuandoExistirEmailCadastrado() {
		//cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		/**Usuario usuario = Usuario.builder()
				.nome("Dionlan Alves de Jesus")
				.email("dionlan.alves@gmail.com")
				.senha("dionlan")
				.dataCadastro(LocalDateTime.now())
				.build(); 
			
				
			usuarioRepository.save(usuario);	
				*/
		//ação / execução
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			usuarioService.validarEmail("dionlan.alves@gmail.com");
		});
	}
}
