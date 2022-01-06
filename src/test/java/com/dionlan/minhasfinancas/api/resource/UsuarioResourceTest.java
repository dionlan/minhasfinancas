package com.dionlan.minhasfinancas.api.resource;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.entity.dto.UsuarioDTO;
import com.dionlan.minhasfinancas.domain.exception.ErroAutenticacao;
import com.dionlan.minhasfinancas.domain.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.domain.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioResourceTest {

	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UsuarioService usuarioService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		//cenário
		String nome = "Dionlan Alves de Jesus";
		String email = "dionlan@dionlan.com";
		String senha = "dionlanSenha";
		UsuarioDTO dto = criaUsuarioDTO();
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha(senha);
		
		Mockito.when(usuarioService.autenticar(email, senha)).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Verificação // Execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar"))
											.accept(JSON)
											.contentType(JSON)
											.content(json);
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId() ))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome() ))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail() ));
	}
	
	@Test
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
		//cenário
		String email = "dionlan@dionlan.com";
		String senha = "dionlanSenha";
		UsuarioDTO dto = criaUsuarioDTO();
		
		Mockito.when(usuarioService.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Verificação // Execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar"))
											.accept(JSON)
											.contentType(JSON)
											.content(json);
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception {
		//cenário
		String nome = "Dionlan Alves de Jesus";
		String email = "dionlan@dionlan.com";
		String senha = "dionlanSenha";
		
		UsuarioDTO dto = criaUsuarioDTO();
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha(senha);
		
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Verificação // Execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/salvar"))
											.accept(JSON)
											.contentType(JSON)
											.content(json);
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId() ))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome() ))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail() ));
	}
	
	@Test
	public void deveRetornarUmBadRequest_QuandoTentarCriarUmNovoUsuario() throws Exception {
		//cenário
		String nome = "usuario mock";
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = criaUsuarioDTO();
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha(senha);
		
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Verificação // Execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/salvar"))
											.accept(JSON)
											.contentType(JSON)
											.content(json);
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	public UsuarioDTO criaUsuarioDTO() {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setNome("Dionlan Alves de Jesus");
		usuarioDTO.setEmail("dionlan@dionlan.com");
		usuarioDTO.setSenha("dionlanSenha");
		
		return usuarioDTO;
	}
}
