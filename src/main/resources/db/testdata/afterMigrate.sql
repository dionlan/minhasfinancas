set foreign_key_checks = 0;

delete from usuario;
delete from lancamento;

set foreign_key_checks = 1;

alter table usuario auto_increment = 1;
alter table lancamento auto_increment = 1;

insert into usuario (id, nome, email, senha, data_cadastro) values (1, 'Dionlan Alves de Jesus', 'dionlan@dionlan.com', 'senhaDionlan', current_timestamp);
insert into usuario (id, nome, email, senha, data_cadastro) values (2, 'Brenda Ribeiro', 'brenda@brenda.com', 'senhaBrenda', current_timestamp);
insert into usuario (id, nome, email, senha, data_cadastro) values (3, 'Afrânio Alves', 'afranio@afranio.com', 'senhaAfraio', current_timestamp);
insert into usuario (id, nome, email, senha, data_cadastro) values (4, 'Guilherme Jonathan', 'guilherme@guilherme.com', 'senhaGuilherme', current_timestamp);


insert into lancamento (id, descricao, ano, mes, valor, id_usuario, status_lancamento, tipo_lancamento, data_cadastro) values (1, 'Fatura Vivo Celular', '2021', '12', 90.99, 1, 'EFETIVADO', 'DESPESA', current_timestamp);
insert into lancamento (id, descricao, ano, mes, valor, id_usuario, status_lancamento, tipo_lancamento, data_cadastro) values (2, 'Conta de Luz', '2021', '12', 96.98, 1, 'EFETIVADO', 'DESPESA', current_timestamp);
insert into lancamento (id, descricao, ano, mes, valor, id_usuario, status_lancamento, tipo_lancamento, data_cadastro) values (3, 'Conta de Água', '2021', '12', 78.88, 1, 'EFETIVADO', 'DESPESA', current_timestamp);
insert into lancamento (id, descricao, ano, mes, valor, id_usuario, status_lancamento, tipo_lancamento, data_cadastro) values (4, 'Internet Casa', '2022', '01', 99.99, 1, 'PENDENTE', 'DESPESA', current_timestamp);
insert into lancamento (id, descricao, ano, mes, valor, id_usuario, status_lancamento, tipo_lancamento, data_cadastro) values (5, 'Academia', '2021', '12', 115.55, 1, 'EFETIVADO', 'DESPESA', current_timestamp);
insert into lancamento (id, descricao, ano, mes, valor, id_usuario, status_lancamento, tipo_lancamento, data_cadastro) values (6, 'Salário GDF', '2021', '12', 7000, 1, 'EFETIVADO', 'RECEITA', current_timestamp);
insert into lancamento (id, descricao, ano, mes, valor, id_usuario, status_lancamento, tipo_lancamento, data_cadastro) values (7, 'Voluntário GDF', '2022', '01', 1200, 1, 'PENDENTE', 'RECEITA', current_timestamp);
insert into lancamento (id, descricao, ano, mes, valor, id_usuario, status_lancamento, tipo_lancamento, data_cadastro) values (8, 'Mensalidade Inglês', '2022', '01', 249.15, 1, 'PENDENTE', 'DESPESA', current_timestamp);