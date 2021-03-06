package com.dionlan.minhasfinancas.api.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dionlan.minhasfinancas.api.assembler.LancamentoInputDTODisassembler;
import com.dionlan.minhasfinancas.api.assembler.LancamentoInput;
import com.dionlan.minhasfinancas.api.assembler.StatusLancamentoInput;
import com.dionlan.minhasfinancas.domain.entity.Lancamento;
import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.entity.dto.LancamentoDTO;
import com.dionlan.minhasfinancas.domain.entity.dto.LancamentoSaidaDTO;
import com.dionlan.minhasfinancas.domain.enums.StatusLancamento;
import com.dionlan.minhasfinancas.domain.enums.TipoLancamento;
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
	private LancamentoSaidaDTO lancamentoSaidaDTO;
	
	@Autowired
	private LancamentoInputDTODisassembler lancamentoEntradaDTODisassembler;
	
	@Autowired
	private UsuarioService usuarioService;
	
	
	@GetMapping("/lancamentos")
	public List<LancamentoDTO> listar(){
		return lancamentoSaidaDTO.converteParaColecaode(service.listar());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/salvar")
	public LancamentoDTO salvar(@RequestBody @Valid LancamentoInput lancamentoInput){
		try {
			Lancamento entidadeLancamento = lancamentoEntradaDTODisassembler.converteDtoParaEntidade(lancamentoInput);
			return lancamentoSaidaDTO.converteParaDto(service.salvar(entidadeLancamento));

		}catch(UsuarioNaoEncontradoException e) {
			throw new RegraNegocioException(e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public LancamentoDTO buscar(@PathVariable Long id) {
		Lancamento lancamento = service.obterPorId(id);
		
		return lancamentoSaidaDTO.converteParaDto(lancamento);
	}
	
	@GetMapping("/lancamentos/{id_usuario}")
	public List<LancamentoDTO> buscarLancamentosPorUsuario(@PathVariable(name = "id_usuario") Long id_usuario) {
		
		Usuario usuario = usuarioService.buscarOuFalhar(id_usuario);
		Lancamento lancamento = new Lancamento();
		lancamento.setUsuario(usuario);
		List<Lancamento> lancamentos = service.buscar(lancamento); 
		
		return lancamentoSaidaDTO.converteParaColecaode(lancamentos);
		 
	}
	
	@GetMapping
	public List<LancamentoDTO> buscar(@RequestParam(value = "descricao", required = false) String descricao,
								@RequestParam(value = "mes", required = false) Integer mes,
								@RequestParam(value = "ano", required = false) Integer ano,
								@RequestParam(value = "tipo", required = false) TipoLancamento tipo,
								@RequestParam(value = "usuario", required = true) Long id){
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setTipo(tipo);
		
		Usuario usuario = usuarioService.buscarOuFalhar(id);
		lancamentoFiltro.setUsuario(usuario);
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		
		return lancamentoSaidaDTO.converteParaColecaode(lancamentos);
		
		
	}

	
	@PutMapping("/{id}")
	public LancamentoDTO atualizar(@PathVariable Long id, @RequestBody LancamentoInput lancamentoInput){
		try {
			Lancamento lancamentoAtual = service.obterPorId(id);
			
			lancamentoEntradaDTODisassembler.converteDtoParaEntidadeParaAtualizacao(lancamentoInput, lancamentoAtual);
			
			return lancamentoSaidaDTO.converteParaDto(service.atualizar(lancamentoAtual));
			
		}catch(UsuarioNaoEncontradoException e) {
			throw new RegraNegocioException(e.getMessage());
		}
	}
	
	@PutMapping("/{id}/atualiza-status")
	public LancamentoDTO atualizarStatus(@PathVariable Long id, @RequestBody StatusLancamentoInput status){
		try {
			Lancamento lancamentoAtual = service.obterPorId(id);
			
			StatusLancamento statusLancamento = StatusLancamento.valueOf(status.getStatus());
			
			lancamentoAtual.setStatus(statusLancamento);
			
			return lancamentoSaidaDTO.converteParaDto(service.atualizar(lancamentoAtual));
			
		}catch(UsuarioNaoEncontradoException e) {
			throw new RegraNegocioException(e.getMessage());
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
							.orElseThrow(() -> new RegraNegocioException("Usu??rio n??o encontrado para o Id informado."));
		lancamento.setUsuario(usuario);
		lancamento.setTipo(TipoLancamento.valueOf(lancamentoDto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(lancamentoDto.getStatus()));
		
		return lancamento;
	} */
	
}
