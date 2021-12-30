set foreign_key_checks = 0;

delete from usuario;
delete from lancamento;

set foreign_key_checks = 1;

alter table usuario auto_increment = 1;
alter table lancamento auto_increment = 1;

insert into usuario (id, nome, email, senha, data_cadastro) values (1, 'Nome Teste', 'email@teste.com.br', 'senhaTeste', current_timestamp);

insert into lancamento (id, descricao, ano, mes, valor, id_usuario, status_lancamento, tipo_lancamento, data_cadastro) values (1, 'Lancamento Descricao', '2020', '01', 10.88, 1, 'PENDENTE', 'RECEITA', current_timestamp);
