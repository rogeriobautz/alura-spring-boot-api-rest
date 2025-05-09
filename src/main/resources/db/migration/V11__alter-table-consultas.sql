alter table consultas add column data_hora datetime not null default CURRENT_TIMESTAMP;

-- Convert date to datetime with time set to noon (12:00:00)
update consultas set data_hora = TIMESTAMP(data, '12:00:00') where data is not null;

alter table consultas drop column data;