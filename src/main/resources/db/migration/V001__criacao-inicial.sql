create table usuario (
	id bigint not null auto_increment,
    nome varchar(60) not null,
    email varchar(60) not null,
    senha varchar(60) not null,
    data_cadastro datetime not null,
    
    primary key(id) 
) engine=InnoDB default charset=utf8;

create table lancamento (
	id bigint not null auto_increment,
    descricao varchar(60) not null,
    ano bigint not null,
    mes bigint not null,
    valor decimal(10,2) not null,
    id_usuario bigint not null,
    status_lancamento varchar(60) not null,
    tipo_lancamento varchar(60) not null,
    data_cadastro datetime not null,
    
    primary key(id) 
) engine=InnoDB default charset=utf8;

alter table lancamento add constraint fk_lancamento_usuario
foreign key (id_usuario) references usuario (id);