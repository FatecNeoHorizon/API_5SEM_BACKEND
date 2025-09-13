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

## Observações

- As configurações de conexão estão protegidas no arquivo `application.properties` e usam variáveis do `.env`.
- Não é necessário configuração adicional.
- A API estará disponível na porta padrão configurada.