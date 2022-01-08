package com.dionlan.minhasfinancas.api.resource;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dionlan.minhasfinancas.api.assembler.UsuarioInput;
import com.dionlan.minhasfinancas.api.assembler.UsuarioInputDTODisassembler;
import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.entity.dto.UsuarioDTO;
import com.dionlan.minhasfinancas.domain.entity.dto.UsuarioSaidaDTO;
import com.dionlan.minhasfinancas.domain.exception.ErroAutenticacao;
import com.dionlan.minhasfinancas.domain.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.domain.service.LancamentoService;
import com.dionlan.minhasfinancas.domain.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private UsuarioInputDTODisassembler usuarioInputDTODisassembler;
	
	@Autowired
	private UsuarioSaidaDTO usuarioDtoDisassembler;
	
	@PostMapping("/autenticar")
	public UsuarioDTO autenticar(@RequestBody UsuarioInput usuarioInput){
		
		try {
			Usuario usuarioAutenticado = usuarioInputDTODisassembler.converteDtoParaEntidade(usuarioInput);
			return usuarioDtoDisassembler.converteParaDto(service.autenticar(usuarioAutenticado.getEmail(), usuarioAutenticado.getSenha()));
			
		}catch(ErroAutenticacao e) {
			throw new RegraNegocioException(e.getMessage());
		}
	}

	@PostMapping("/salvar")
	public UsuarioDTO salvar(@RequestBody UsuarioInput usuarioInput) {
		
		Usuario usuarioCadastrado = usuarioInputDTODisassembler.converteDtoParaEntidade(usuarioInput);
		
		return usuarioDtoDisassembler.converteParaDto(service.salvarUsuario(usuarioCadastrado));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		service.deletar(id);
	}
	
	@GetMapping("/{id}")
	public Usuario buscaUsuarioPorId(@PathVariable("id") Long id) {
		Usuario usuario = service.buscarOuFalhar(id);
		return usuario;
	}
	
	@GetMapping("/{id}/saldo")
	public BigDecimal obterSaldo(@PathVariable("id") Long id) {
		service.buscarOuFalhar(id);
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return saldo;
	}
	
}
