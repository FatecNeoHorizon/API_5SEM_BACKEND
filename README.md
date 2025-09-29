# API_5SEM_BACKEND

## Como clonar o repositório

1. Abra o terminal e execute:
	```sh
	git clone https://github.com/FatecNeoHorizon/API_5SEM_BACKEND.git
	```

2. Acesse a pasta do projeto:
	```sh
	cd API_5SEM_BACKEND
	```

# Como rodar a aplicação

## Pré-requisitos

- Java 21
- Maven
- PostgreSQL

## Passos para rodar

1. Acesse a pasta `api` dentro da raiz do projeto:
	```sh
	cd api
	```

2. Copie o arquivo `.env.example` para `.env` e preencha com os dados do seu banco PostgreSQL:
	```sh
	cp .env.example .env
	# Ou crie manualmente o arquivo .env
	```

3. Certifique-se que o banco PostgreSQL está rodando e acessível.

4. Execute o comando para iniciar a aplicação:
	```sh
	mvn spring-boot:run
	```


## Documentação da API com Swagger

Esta API utiliza o Swagger para documentação e testes interativos dos endpoints.

### Como acessar o Swagger

Após iniciar a aplicação, acesse:

- Interface Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Documentação dos endpoints: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)



O Swagger é uma ferramenta essencial para APIs REST, pois:

- Permite explorar todos os endpoints de forma visual e interativa.
- Facilita o teste das operações diretamente pelo navegador, sem necessidade de ferramentas externas.
- Exibe exemplos de requisição e resposta, parâmetros e modelos de dados.
- Ajuda desenvolvedores e equipes a entenderem rapidamente como utilizar a API.

Com o Swagger, qualquer pessoa pode experimentar os recursos do CRUD, validar integrações e aprender sobre a API sem precisar consultar a documentação técnica tradicional.