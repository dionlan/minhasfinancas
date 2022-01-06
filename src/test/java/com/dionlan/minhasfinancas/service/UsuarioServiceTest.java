package com.dionlan.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.exception.ErroAutenticacao;
import com.dionlan.minhasfinancas.domain.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.domain.repository.UsuarioRepository;
import com.dionlan.minhasfinancas.domain.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
public class UsuarioServiceTest {

	@SpyBean
	private UsuarioServiceImpl usuarioService;
	
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	@Test
	public void deveSalvarUmUsuario(){
		//cenário
		
		Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("nome");
		usuario.setEmail("email@email.com");
		usuario.setSenha("senha");
		
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//ação / execução
		Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
		
		//verificação
		assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	/**
	 * espera-se que lance a exception e nunca chame o método de salvar, pois o e-mail informado já está cadastrado
	 */
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado(){
		//cenário
		String email = "dionlan@dionlan.com";
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);
		
		//ação / execução
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			usuarioService.salvarUsuario(usuario);
		});
		
		//verificação
		Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		String email = "emailAindaNaoCadastrado@emailAindaNaoCadastrado.com";
		String senha = "senhaUsuarioTesteIngração";
		
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(senha);
		
		Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(usuario);
		
		//ação / execução
		Usuario usuarioAutenticado = usuarioService.autenticar(email, senha);
		
		//verificação
		assertThat(usuarioAutenticado).isNotNull();
	}
	/**
	 * teste para cair na exceção ErroAutenticacao quando encontrar um usuario já cadastrado com o email informado
	 */
	@Test
	public void deveLancarErro_QuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		//cenário
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(null);
		
		//ação / execução
		 Throwable exception = catchThrowable(() -> usuarioService.autenticar("asdf@asdf.com", "123"));
		 
		 assertThat(exception)
		 .isInstanceOf(ErroAutenticacao.class)
        .hasMessage("Usuário não encontrado para o e-mail informado.");
	}
	
	@Test
	public void deveLancarErro_QuandoSenhaNaoBater() {
		//cenário
		String senha = "dionlan";
		Usuario usuario = new Usuario();
		usuario.setEmail("dionlan@dionlan.com");
		usuario.setSenha(senha);
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(usuario);
		
		//ação / execução
		 Throwable exception = catchThrowable(() -> usuarioService.autenticar("dionlan@dionlan.com", "123"));
		 
		 assertThat(exception)
		 .isInstanceOf(ErroAutenticacao.class)
         .hasMessage("Senha incorreta.");
		
	}
	
	/**
	 * Espera-se que não lance nenhuma exceção, pois nao haverá nenhum email cadastrado no teste
	 */
	@Test
	public void deveValidarEmail_QuandoNaoHouverOutroEmailIgualCadastrado() {
		//cenário -> simula que não existe o email informado na base de dados (returna falso)
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação / execução
		usuarioService.validarEmail("dionlan@dionlan.com");
		
	}
	
	/**
	 * Espera-se que lance a exceção, pois haverá um email identico já cadastrado no teste
	 */
	@Test
	public void deveLancarErroAoValidarEmail_QuandoExistirEmailCadastrado() {
		//cenário -> simula que não existe o email informado na base de dados (retorna true)
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//ação / execução
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			usuarioService.validarEmail("dionlan@dionlan.com");
		});
	}
	
	public void criaESalvaUsuario() {
		Usuario usuario = new Usuario();
		
		usuario.setNome("Usuario Teste de Integração");
		usuario.setEmail("dionlan@dionlan.com");
		usuario.setSenha("senhaUsuarioTesteIngração");
		usuario.setDataCadastro(OffsetDateTime.now());
		usuarioService.salvarUsuario(usuario);
	}
}
