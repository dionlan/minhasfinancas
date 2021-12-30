package com.dionlan.minhasfinancas.api.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dionlan.minhasfinancas.api.assembler.LancamentoEntradaDTODisassembler;
import com.dionlan.minhasfinancas.api.assembler.LancamentoInput;
import com.dionlan.minhasfinancas.domain.entity.Lancamento;
import com.dionlan.minhasfinancas.domain.entity.dto.LancamentoDTO;
import com.dionlan.minhasfinancas.domain.entity.dto.LancamentoSaidaDTO;
import com.dionlan.minhasfinancas.domain.exception.NegocioException;
import com.dionlan.minhasfinancas.domain.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.domain.exception.UsuarioNaoEncontradoException;
import com.dionlan.minhasfinancas.domain.service.LancamentoService;
import com.dionlan.minhasfinancas.domain.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoService service;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private LancamentoSaidaDTO lancamentoSaidaDTO;
	
	@Autowired
	private LancamentoEntradaDTODisassembler lancamentoEntradaDTODisassembler;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LancamentoDTO salvar(@RequestBody @Valid LancamentoInput lancamentoInput){
		try {
			Lancamento entidadeLancamento = lancamentoEntradaDTODisassembler.converteDtoParaEntidade(lancamentoInput);
			return lancamentoSaidaDTO.converteParaDto(service.salvar(entidadeLancamento));

		}catch(UsuarioNaoEncontradoException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public LancamentoDTO atualizar(@PathVariable Long id, @RequestBody LancamentoDTO lancamentoDto){
		try {
			Lancamento lancamentoAtual = service.obterPorId(id);
			lancamentoEntradaDTODisassembler.converteDtoParaEntidadeParaAtualizacao(lancamentoDto, lancamentoAtual);
			return lancamentoSaidaDTO.converteParaDto(lancamentoAtual);
			
		}catch(RegraNegocioException e) {
			throw new RegraNegocioException("Usuário não encontrado para o Id informado.");
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable("id") Long id){
		service.deletar(id);
	}
	
	/*
	private Lancamento dtoToLancamento(LancamentoDTO lancamentoDto) {
		Lancamento lancamento = Lancamento.builder()
				.id(lancamentoDto.getId())
				.descricao(lancamentoDto.getDescricao())
				.ano(lancamentoDto.getAno())
				.mes(lancamentoDto.getMes())
				.valor(lancamentoDto.getValor())
				.build();
		
		Usuario usuario = usuarioService
							.obterPorId(lancamentoDto.getUsuario())
							.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado."));
		lancamento.setUsuario(usuario);
		lancamento.setTipo(TipoLancamento.valueOf(lancamentoDto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(lancamentoDto.getStatus()));
		
		return lancamento;
	} */
	
}
