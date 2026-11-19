# Store Operations System

**Languages:** English | [Português (Brasil)](README.pt-BR.md)

An applied learning project: we are building a Java system to support inventory, goods receiving, and order fulfillment, making decisions as we would in a real-world project.

## First iteration goals

- Register products and their units of measure.
- Record goods receiving and verify received quantities.
- Record physical stock intake by typing a product name, searching for and selecting an existing product or creating a new one, then entering the quantity and storage allocation.
- Track stock balances and movement history.
- Guide physical picking before an invoice is issued, then link the sales XML afterward.
- Check and pack orders and record fulfillment events.
- Manually import a sales invoice XML when needed to associate the invoice with a sale and generate a checking label. Goods intake does not depend on the supplier's XML.
- Record the expected marketplace payout for each sale and reconcile the actual credit by sale ID against the bank statement.
- Reconcile transactions using manual bank statement entries in the first iteration.
- Keep packing videos linked to the sale for three months.
- Store videos on persistent local server storage and delete them automatically after three months.
- Monitor services using centralized logs, metrics, and traces in Grafana.
- Review operations and track who performed each action.

## Project organization

The backend is the primary focus. The front end will be built with React and kept separate from the backend. Python may be introduced later for an integration or task where it provides a clear benefit, such as data analysis or batch processing; we will not create a Python service without a defined business need.

Before implementation, we will use `docs/` to document processes, business rules, the domain model, diagrams, and technical decisions. Initial diagrams and rules are hypotheses to be validated against real examples from the store's operations.

Current analysis documents (currently written in Portuguese):

- [Scope and domain](docs/01-escopo-e-dominio.md)
- [Domain model and workflows](docs/02-modelo-do-dominio.md)
- [Operational scenarios](docs/03-cenarios-operacionais.md)
- [Microservice boundaries](docs/04-fronteiras-de-servicos.md)
- [Observability with Grafana](docs/05-observabilidade.md)

Planned structure, subject to decisions about the monorepo and service boundaries:

```text
MF_Sistema/
├── docs/                  # processes, rules, diagrams, and decisions
├── backend/               # Java/Spring Boot services
├── frontend/              # React application
└── python/                # only when there is a clear use case
```

## Local development

The current Docker Compose setup runs the `catalog-service` and its PostgreSQL 17 database. The Compose file is at the repository root; the local `.env` file there supplies `POSTGRES_PASSWORD` and is ignored by Git. Flyway manages the database schema through versioned SQL files in `backend/catalog-service/src/main/resources/db/migration`. Migration `V1__create_produtos.sql` creates the initial `produtos` table, and `V2__add_franquia_to_produtos.sql` adds the product franchise.

Run these commands from the repository root:

```powershell
docker compose up --build -d
docker compose ps
docker compose logs --tail=100 catalog-service
docker compose exec catalog-db psql -U catalog_user -d catalog_db
```

The service is published at `http://localhost:8081` and PostgreSQL at `localhost:5432`. Catalog routes include `GET /produtos`, `GET /produtos/{id}`, searches by name, franchise, and figure number, `POST /produtos`, `PUT /produtos/{id}`, `DELETE /produtos/{id}` (logical deactivation), and `PATCH /produtos/{id}/ativar`. Delete and activation return `204 No Content`. The Maven suite has 23 passing tests across context, service, and validation; routes were also exercised manually against PostgreSQL. The named `catalog_db_data` volume keeps database data when containers stop. `docker compose down -v` also deletes that volume and its data. The `produto_laboratorio` table used for SQL practice was temporary and is not part of the application schema.

## Initial architecture

The system will be divided by business capabilities. The breakdown below is a starting hypothesis and will be validated as we learn more about the store's rules.

| Service | Main responsibility |
| --- | --- |
| `discovery-server` | Service registration and discovery with Eureka. |
| `config-server` | External, shared configuration for services. |
| `api-gateway` | HTTP entry point, routing, and shared API policies. |
| `rabbitmq` | Durable messaging for inter-service events, with retries and a dead-letter queue. |
| `catalog-service` | Product registration and lookup. |
| `inventory-service` | Physical stock by box/position, balances, and movements. |
| `receiving-service` | Recording, checking, and allocating received goods. |
| `sales-service` | Online sales confirmed through manually imported XML and in-store sales recorded in the system. |
| `fulfillment-service` | Order checking, packing, and process evidence. |
| `finance-service` | Marketplace payouts and reconciliation against bank statement entries. |

Each service will have clear boundaries and own its data. Olist remains responsible for online orders, invoice issuance, and marketplace shipping labels. The new system records goods intake manually and imports sales XML when needed to support checking, packing, and financial reconciliation. Synchronous calls handle lookups and immediate responses; RabbitMQ distributes durable business events between services using an outbox, acknowledgements, retries, and idempotent consumers.

## Implementation principles

- Java with Spring Boot and Maven; use Lombok to reduce repetitive code when it improves readability.
- Apply SOLID principles and design patterns to solve concrete problems, without introducing abstractions without a need.
- No setters in domain classes. Changes will be expressed through business methods such as `confirmReceiving()` or `reserve(...)`.
- Data corrections will be explicit domain actions, not generic setters. The entity validates the change; the use case coordinates persistence and history; the repository handles persistence separately.
- Prefer immutability where it makes sense. Persisted entities may need specific constructors and JPA mappings; we will handle that without exposing public setters.
- Use inheritance sparingly, when there is a stable “is-a” relationship. Prefer composition when it better represents the domain.
- Versioned APIs, input validation, consistent error handling, logs, and automated tests focused on business rules.
- Automated tests are part of the project. On 2026-11-19, `mvn test` passed with 23 tests covering context, service, and validation. Controller tests remain to be written.
- Use Docker and Docker Compose to run the application and its dependencies locally.

## Development roadmap

1. Understand the store's current operations and map workflows and rules.
2. Define the first functional slice and the states of each process.
3. Prepare the Maven structure, conventions, and local Docker setup.
4. Implement a small vertical slice, from the endpoint through persistence and business rules.
5. Cover and exercise real scenarios with tests, such as receiving goods, reusing empty positions, insufficient stock, cancellation, and reconciliation discrepancies.
6. Expand observability, security, and inter-service integration as needed.

## Open decisions

- Detail the cases and effects of cancellations and returns at each stage of the workflow.
- Detail the actions allowed in each functional module, starting with Inventory, Finance, and other system areas.
- Choose the location of the dedicated local video volume during server deployment.

These decisions define the first workflow we will implement. The architecture is intentionally an initial proposal and may change to reflect the store's actual rules.
