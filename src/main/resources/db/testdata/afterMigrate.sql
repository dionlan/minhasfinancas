set foreign_key_checks = 0;

delete from usuario;

set foreign_key_checks = 1;

alter table usuario auto_increment = 1;

insert into usuario (id, nome, email, senha, data_cadastro) values (1, 'Nome Teste', 'email@teste.com.br', 'senhaTeste', current_timestamp);