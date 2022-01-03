package com.dionlan.minhasfinancas.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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

import com.dionlan.minhasfinancas.domain.entity.Lancamento;
import com.dionlan.minhasfinancas.domain.entity.Usuario;
import com.dionlan.minhasfinancas.domain.enums.StatusLancamento;
import com.dionlan.minhasfinancas.domain.enums.TipoLancamento;
import com.dionlan.minhasfinancas.domain.repository.LancamentoRepository;
import com.dionlan.minhasfinancas.domain.repository.UsuarioRepository;

@DataJpaTest //cria uma transação na base de dados, executa o teste e após faz o rollback do estado antes de cada teste, pode-se retirar o método deleteAll();
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {

	@Autowired
	private LancamentoRepository repository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void deveSalvarLancamento() {
		
		Lancamento lancamento = criarEPersistirLancamento();
		
		lancamento = repository.save(lancamento);
		
		assertThat(lancamento.getId()).isNotNull();
	}
	/**
	 * Para deletar, primeiro cria-se o lançamento persistindo na base de dados;
	 * Após verifica recupera pelo id o lançamento persistido para ser deletado. Por fim realiza-se a consulata pelo id para certificar que foi realmente deletado e o id eh null.
	 * 
	 *
	 */
	@Test
	public void deveDeletarUmLancamento() {
	
		//cenário: cria e recupera um lançamento para ser deletado
		Lancamento lancamento = criarEPersistirLancamento();
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		//ação / execução	: deleta o lançamento
		repository.delete(lancamento);
		
		//verificação: conferência se o id do lançamento deletado realmente é nulo, se for, o teste está aprovado
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());	
		assertThat(lancamentoInexistente).isNull();
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		//cenário: cria e recupera um lançamento para ser deletado
		Lancamento lancamento = criarEPersistirLancamento();
		lancamento.setAno(2018);
		lancamento.setDescricao("Teste Atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualiado = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoAtualiado.getAno()).isEqualTo(2018);
		assertThat(lancamentoAtualiado.getDescricao()).isEqualTo("Teste Atualizar");
		assertThat(lancamentoAtualiado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
		
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarEPersistirLancamento();
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
		
	}
	
	public Lancamento criarEPersistirLancamento() {
		Lancamento lancamento = new Lancamento();

		lancamento.setAno(2009);
		lancamento.setMes(1);
		lancamento.setDescricao("Lançamento Qualquer Teste");
		lancamento.setValor(BigDecimal.valueOf(10));
		lancamento.setTipo(TipoLancamento.RECEITA);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		lancamento.setDataCadastro(OffsetDateTime.now());
		lancamento.setUsuario(recuperarUsuario());
		 
		entityManager.persist(lancamento);
		return lancamento;
	}
	
	public Usuario recuperarUsuario() {

		Optional<Usuario> usuarioOptional = usuarioRepository.findById(1L);
		Usuario usuario = new Usuario();
		usuario.setId(usuarioOptional.get().getId());
		usuario.setEmail(usuarioOptional.get().getEmail());
		usuario.setNome(usuarioOptional.get().getNome());
		usuario.setSenha(usuarioOptional.get().getSenha());
		usuario.setDataCadastro(OffsetDateTime.now());
		usuario.setLancamentos(usuarioOptional.get().getLancamentos());
		
		return usuario;
	}
	
}
