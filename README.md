# Sirio Libânes

## Documentação

Github:

* [Clonar o projeto do Github](https://github.com/wagnerlsr/sirio-libanes)

###
## Execução do banco de dados

Antes de subir o aplicatico acessar o diretorio do projeto e executar o comando abaixo:
* `docker-compose up -d`

Parando o banco de dados:
* `docker-compose down`

####
## Instruções para build e execução do projeto

Para buildar o aplicatico acessar o diretorio do projeto e executar o comando abaixo:
* `mvn clean install`

Após o build entrar na pasta target/ e executar a aplicação:
* `java -jar sirio-0.0.1-SNAPSHOT.jar`

Para executar os testes unitários:
* `mvn test`

###
## Acesso a aplicação e execução dos testes

Para acessar o cliente da API usar o link abaixo:
* Link de acesso ao portal - [http://localhost:3000/swagger-ui/index.html](http://localhost:3000/swagger-ui/index.html)
* [POST /login](http://localhost:3000/swagger-ui/index.html#/user-controller/authenticateUser) - endpoint de login para recuparação do token de acesso

## Endpoints com a role ADMIN para acesso por usuários com perfil de administrador:
* [POST /signup](http://localhost:3000/swagger-ui/index.html#/user-controller/createUser) - endpoint para criação de um usuário
* [GET /users](http://localhost:3000/swagger-ui/index.html#/user-controller/getUsers) - endpoint para listar todos usuários
* [DELETE /users/{cpf}](http://localhost:3000/swagger-ui/index.html#/user-controller/removeUser) - endpoint para criação de um usuário

## Endpoints com role USER para acesso dos usuários normais:
Um usuário só pode acessar e alterar os próprios dados
* [GET /users/{cpf}](http://localhost:3000/swagger-ui/index.html#/user-controller/removeUser) - endpoint para listar um usuário
* [PATCH /users/{cpf}](http://localhost:3000/swagger-ui/index.html#/user-controller/updateUser) - endpoint para atualizar dados do usuário

##
## Informações gerais
Quando aplicação é executada são criados três registros no banco de dados para manutenção dos sistema:
####
Serão criadas duas Roles
* `ADMIN - para usuários com perfil de administrador`
* `USER - para usuários normais`

Será criado um usário com perfil administrador
* `Name: Admin`
* `CPF: 99999999999`
* `Password: admin`

##
## Justificativas
Nos endpoints que retornam dados dos usuários estão sendo retornados todos os campos da entidade
para facilitar a visualização dos dados testados. 
