# Desafio de Votação (Sicredi) 🗳️

[![Build](https://img.shields.io/github/actions/workflow/status/Gilberto491/DesafioVotacao/ci.yml?branch=develop)](https://github.com/Gilberto491/DesafioVotacao/actions)
[![Docker Ready](https://img.shields.io/badge/docker-ready-blue)](#execução-com-docker)

## 📌 Apresentação do Desafio
Solução para gestão de sessões de votação em cooperativismo:
- Cada associado vota uma única vez por pauta (Sim/Não).
- Abertura de sessões com duração configurável (default 1 minuto).
- Contabilização de votos e apuração do resultado.

> **Objetivo:** expor uma API REST escalável, observável e preparada para cloud.

---

## 🧰 Tecnologias e Ferramentas
- **Linguagem/Framework:** Java 17, Spring Boot (Web, Data JPA, Validation, Actuator)
- **Banco:** H2 (dev/test), PostgreSQL (prod)
- **Migração:** Flyway
- **Build/Testes:** Maven, JUnit 5, Mockito
- **Observabilidade:** Prometheus, Grafana, API (health)
- **Performance:** k6
- **Container/Orquestração:** Docker/Podman + Docker Compose
- **Docs:** OpenAPI/Swagger

## 🌐 URLs Online
- 📊 **API Base:** <a href="http://34.61.3.188:8080/api/v1" target="_blank">API Base</a>
- 📑 **Swagger UI:** <a href="http://34.61.3.188:8080/swagger-ui/index.html" target="_blank">Swagger UI</a>
- 📈 **Prometheus:** <a href="http://34.61.3.188:9090" target="_blank">Prometheus</a>
- 📊 **Grafana:** <a href="http://34.61.3.188:3000" target="_blank">Grafana</a></li>
- ❤️ **Healthcheck:** <a href="http://34.61.3.188:8080/actuator/health" target="_blank">Healthcheck</a>

## 🔗 Endpoints da API

### 📂 Topic
- `POST /api/v1/topics` — Create Topic
- `GET /api/v1/topics` — List Topics
- `GET /api/v1/topics/{id}` — Topic By Id
- `DELETE /api/v1/topics/{id}` — Delete Topic

### 📂 Session
- `POST /api/v1/sessions` — Create Session

### 📂 Vote
- `POST /api/v1/votes/check-open` — Check Open
- `GET /api/v1/votes/open-now` — Open Now
- `POST /api/v1/votes` — Vote Topic
- `GET /api/v1/votes/count` — Count Vote

  > **Detalhes completos:** consulte o Swagger.

  ## ✨ Diferenciais do Projeto
- **Migrations com Flyway** (versionadas e idempotentes)
- **Mensagens em `messages.properties`** (i18n de erros/validações)
- **Padrão de logs** (correlação, níveis e formatação para observabilidade)
- **Actuator + Métricas** expostas para Prometheus
- **Testes**: unidade e integração (controllers/services/repos)
- **Pipeline CI**: build, testes e versões com tags

---

## 🧪 Testes e Performance

### ✅ Testes automatizados
```bash
k6 run vote-session.js \
  -e BASE_URL=http://localhost:8080 \
  -e SESSION_ID=1 \
  -e VUS=500 \
  -e DURATION=1m
```

KPIs principais analisados:

http_req_duration → tempo médio das requisições
checks → porcentagem de checks que passaram
http_req_failed → taxa de falhas de requisições

  ## 🎯 Tarefas Bônus
- [x] **Validação externa de CPF** (mockado para efeito do desafio)
- [x] **Observabilidade** com Prometheus + Grafana
- [x] **Pipeline CI/CD** com GitHub Actions
- [x] **Migrations com Flyway** para versionamento do schema
- [x] **Mensagens em arquivo properties** (i18n de erros e validações)
- [x] **Testes de performance com k6** (rodados em ambiente local)
- [x] **Controle de versão** (através de tags)

> ⚠️ Os testes de carga foram executados **localmente**. O ambiente de VM na nuvem utiliza plano *free*, sujeito a restrições de disco e desempenho, podendo causar lentidão não relacionada ao código da aplicação.

---

## 🔒 Políticas e Regras de Negócio
- Cada associado pode **votar apenas uma vez por pauta**.  
- Uma sessão só pode ser aberta se a pauta não possuir outra sessão ativa.  
- Ao encerrar, a sessão muda status para **USED** e não pode ser reaberta.  
- Resultados contabilizam todos os votos válidos (`YES` / `NO`).  
