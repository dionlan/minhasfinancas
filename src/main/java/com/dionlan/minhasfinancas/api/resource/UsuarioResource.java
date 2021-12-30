package com.dionlan.minhasfinancas.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.entity.dto.UsuarioDTO;
import com.dionlan.minhasfinancas.domain.exception.ErroAutenticacao;
import com.dionlan.minhasfinancas.domain.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService service;
	
	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar(@RequestBody UsuarioDTO usuarioDto){
		
		try {
			Usuario usuarioAutenticado = service.autenticar(usuarioDto.getEmail(), usuarioDto.getSenha());
			return ResponseEntity.ok().body(usuarioAutenticado);
			
		}catch(ErroAutenticacao e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/salvar")
	public ResponseEntity<?> salvar(@RequestBody UsuarioDTO usuarioDto) {
		/*
		Usuario usuarioEntity = Usuario.builder()
				.email(usuarioDto.getEmail())
				.nome(usuarioDto.getNome())
				.senha(usuarioDto.getSenha())
				.build();
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuarioEntity);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
			
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} */
		return null;
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		service.deletar(id);
	}
	
	
}
