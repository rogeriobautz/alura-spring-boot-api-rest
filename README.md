# Descrição

- Api desenvolvida com base nos cursos de spring da alura
- Extras: cache, swagger, endpoints de usuário.

# Teste local

## Collection
[Insomnia](insomnia/collection/insomnia-collection-api-voll-med.yaml)

## MySQL no docker

### Criação do container
```shell
    docker run --name mysql-database \
    -e MYSQL_ROOT_PASSWORD=root \
    -p 3306:3306 \
    -d mysql:8.0
```

### Criação da database
```shell
docker exec -it mysql-database bash
mysql -u root -p
#senha: root
create database vollmed_api
```

### Migrations
- As [migrations de V1 até V8](src/main/resources/db/migration/V1__create-table-medicos.sql) devem dar conta de criar as tabelas.
- Podem ser simplificadas para caso for começar do zero.