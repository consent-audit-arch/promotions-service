# promotions-service

Cenário de promoções vinculadas a titulares, com avaliação individual e em lote.

Parte do **Consent & Audit Architecture**, projeto pessoal de Renan Rohers iniciado no TCC sobre controle de acesso e auditabilidade em microsserviços.

[Visão geral e execução local](https://github.com/consent-audit-arch/infra#readme) · [Diagramas e decisões](https://github.com/consent-audit-arch/infra/blob/main/docs/architecture.md) · [Console](https://github.com/consent-audit-arch/consent-frontend)

## Papel na arquitetura

Vincula promoções a titulares por `dataSubjectId` e usa o contexto de autorização para consultar perfis. O nome local usado pelos scripts é `promotion-service`; o repositório no GitHub se chama `promotions-service`.

## Tecnologias

Java 21 · Spring Boot · Maven. Integrações deste componente: PostgreSQL, Keycloak, OPA, Kafka, user-service.

## Executar e desenvolver

O [guia central](https://github.com/consent-audit-arch/infra#executar-localmente) configura bancos, mensageria, identidade, imagens e sidecars OPA. No Compose, este serviço fica em `http://localhost:8085`.

Para compilar e executar os testes a partir da raiz deste repositório, instale primeiro o starter no Maven local (os dois repositórios devem estar lado a lado):

```bash
mvn -f ../tcc-security-starter/pom.xml install
mvn verify
```

Os testes de integração podem exigir Docker e dependências externas; consulte as configurações em `src/main/resources`. Para executar o conjunto integrado, use o Compose central.

## API principal

```text
POST /api/v1/promotions
POST /api/v1/promotions/evaluate-batch
GET /api/v1/promotions
GET /api/v1/promotions/{id}
GET /api/v1/promotions/segment/{segment}
GET /api/v1/promotions/{userId}/with-profile
```

A autenticação usa JWT do realm `tcc`. Nos endpoints protegidos pelo PEP, o contexto pode incluir `X-Purpose`, `X-Data-Subject-Id`, `X-Data-Subject-Ids`, `X-Data-Categories` e `X-Correlation-Id`, conforme a operação e a policy. A [coleção Postman](https://github.com/consent-audit-arch/infra/blob/main/postman_collection.json) reúne exemplos.

## Escopo

Protótipo para estudo de autorização contextual e rastreabilidade com dados de demonstração. A cobertura de políticas varia por serviço. Os [limites e próximos passos](https://github.com/consent-audit-arch/infra/blob/main/docs/architecture.md#limites-e-evolução) descrevem as garantias e os pontos ainda em evolução.
