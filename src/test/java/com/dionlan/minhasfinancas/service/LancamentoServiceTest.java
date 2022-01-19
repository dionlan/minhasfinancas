package com.dionlan.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.TestPropertySource;

import com.dionlan.minhasfinancas.domain.entity.Lancamento;
import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.enums.StatusLancamento;
import com.dionlan.minhasfinancas.domain.enums.TipoLancamento;
import com.dionlan.minhasfinancas.domain.exception.RegraNegocioException;
import com.dionlan.minhasfinancas.domain.repository.LancamentoRepository;
import com.dionlan.minhasfinancas.domain.service.LancamentoService;
import com.dionlan.minhasfinancas.domain.service.UsuarioService;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class LancamentoServiceTest {
	
	@SpyBean
	private LancamentoService lancamentoService; //utiliza os métodos reais da classe de serviço
	
	@MockBean
	private LancamentoRepository lancamentoRepository; //simula os comportamentos do repository
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Test
	public void deveSalvarUmLancamento_QuandoNaoHouverErroDeValidacao() {
		//cenário
		Lancamento lancamentoASalvar = criarLancamento();
		Mockito.doNothing().when(lancamentoService).validarLancamento(lancamentoASalvar); //Não faz nada quando chamar a validação do lancamento a salvar
		
		Lancamento lancamentoSalvo = criarLancamento();;
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(lancamentoRepository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//ação / execução
		Lancamento lancamento = lancamentoService.salvar(lancamentoASalvar);
		assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void deveAtualizarUmLancamento_QuandoNaoHouverErroDeValidacao() {
		//cenário
		Lancamento lancamentoSalvo = criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(lancamentoService).validarLancamento(lancamentoSalvo); //Não faz nada quando chamar a validação do lancamento a salvar

		Mockito.when(lancamentoRepository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//Ação / Execução
		lancamentoService.atualizar(lancamentoSalvo);
		
		//verificação
		Mockito.verify(lancamentoRepository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void naoDeveSalvarUmLancamento_QuandoHouverErroDeValidacao() {
		//cenário
		Lancamento lancamentoASalvar = criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(lancamentoService).validarLancamento(lancamentoASalvar);
		
		//execução e verificação
		catchThrowableOfType(() -> lancamentoService.salvar(lancamentoASalvar), RegraNegocioException.class);
		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);
	
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenário
		Lancamento lancamento = criarLancamento(); //não seta o ID
		
		//execução e verificação
		catchThrowableOfType(() -> lancamentoService.atualizar(lancamento), NullPointerException.class);
		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamento);
	
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		//cenário
		Lancamento lancamento = criarLancamento();
		lancamento.setId(1L);
		
		//Ação / Execução
		lancamentoService.deletar(lancamento.getId());
		
		//Verificação
		Mockito.verify(lancamentoRepository).deleteById(lancamento.getId());
		
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenário
		Lancamento lancamento = criarLancamento();
		
		//Ação / Execução
		catchThrowableOfType(() -> lancamentoService.deletar(lancamento.getId()), NullPointerException.class);
		
		//Verificação
		Mockito.verify(lancamentoRepository, Mockito.never()).delete(lancamento);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void deveFiltrarLancamentos() {
		//Cenário
		Lancamento lancamento = criarLancamento();
		lancamento.setId(1L);
		
		List<Lancamento> lancamentos = Arrays.asList(lancamento);
		Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(lancamentos);
		
		//Execução / ação
		List<Lancamento> resultado = lancamentoService.buscar(lancamento);
		
		//verificação
		assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
	}
	
	@Test
	public void deveAtualiarOStatusDeUmLancamento() {
		//cenário
		Lancamento lancamento = criarLancamento();
		lancamento.setId(1L);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(lancamentoService).atualizar(lancamento);
		
		//Ação / execução
		lancamentoService.atualizarStatus(lancamento, novoStatus);
		
		//verificações
		assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(lancamentoService).atualizar(lancamento);
	}
	
	@Test
	public void obterUmLancamentoPorId() {
		//cenário
		Long id = 1L;
		
		Lancamento lancamento = criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//Ação / Execução
		Lancamento resultado = lancamentoService.obterPorId(id);
		
		//Verificação
		assertThat(resultado).isNotNull();
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		//cenário
		Long id = 1L;
		
		Lancamento lancamento = criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.empty());
		
		//Ação / Execução
		Optional<Lancamento> resultado = lancamentoRepository.findById(id);
		
		//Verificação
		assertThat(resultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
		Lancamento lancamento = new Lancamento();
		
		Throwable erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informa uma descrição válida.");
		
		lancamento.setDescricao("");
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informa uma descrição válida.");
		
		lancamento.setDescricao("Salário");
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido.");
		
		lancamento.setMes(0);
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido.");
		
		lancamento.setMes(13);
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido.");
		
		lancamento.setMes(1);
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido.");
		
		lancamento.setAno(20125);
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido.");
		
		lancamento.setAno(2020);
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuário.");
		
		lancamento.setUsuario(new Usuario());
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuário.");
		
		lancamento.getUsuario().setUserId(1L);
		//lancamento.getUsuario().setNome("Teste");
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor válido.");
		
		lancamento.setValor(BigDecimal.ZERO);
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor válido.");
		
		lancamento.setValor(BigDecimal.valueOf(155.87));
		
		erro = catchThrowable(() -> lancamentoService.validarLancamento(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de lançamento.");
		
	}

	public Lancamento criarLancamento() {
		Lancamento lancamento = new Lancamento();

		lancamento.setAno(2009);
		lancamento.setMes(1);
		lancamento.setDescricao("Lançamento Qualquer Teste");
		lancamento.setValor(BigDecimal.valueOf(10));
		lancamento.setTipo(TipoLancamento.RECEITA);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		lancamento.setDataCadastro(OffsetDateTime.now());
		lancamento.setUsuario(recuperarUsuario());

		return lancamento;
	}
	
	public Usuario recuperarUsuario() {

		Usuario usuarioOptional = usuarioService.buscarOuFalhar(1L);
		
		Lancamento lancamento = new Lancamento();
		lancamento.setUsuario(usuarioOptional);
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamento);
		
		Usuario usuario = new Usuario();
		usuario.setUserId(usuarioOptional.getUserId());
		usuario.setEmail(usuarioOptional.getEmail());
		usuario.setNome(usuarioOptional.getNome());
		usuario.setSenha(usuarioOptional.getSenha());
		usuario.setDataCadastro(OffsetDateTime.now());
		usuario.setLancamentos(lancamentos);
		
		return usuario;
	}
}
