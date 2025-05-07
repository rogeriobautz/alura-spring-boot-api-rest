alter table usuarios add ativo tinyint not null default 1;
alter table usuarios add nome varchar(80) not null default "usuario antigo ";
