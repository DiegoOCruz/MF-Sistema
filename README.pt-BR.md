# Sistema de Operação da Loja

**Idiomas:** [English](README.md) | Português (Brasil)

Projeto de aprendizado aplicado: vamos construir um sistema Java para apoiar estoque, recebimento de mercadorias e expedição de pedidos, tomando decisões como em um projeto real.

## Objetivos do primeiro ciclo

- Registrar produtos e suas unidades de medida.
- Receber mercadorias e conferir quantidades recebidas.
- Registrar a entrada física digitando o nome do produto; buscar e selecionar um produto existente ou abrir seu cadastro, depois informar quantidade e alocação.
- Manter saldo de estoque e histórico de movimentações.
- Orientar a retirada física antes da emissão da NF e vincular depois o XML da venda.
- Conferir, embalar e registrar ocorrências do atendimento.
- Importar manualmente o XML da NF de venda quando ele for necessário para associar a nota à venda e gerar a etiqueta de conferência; a entrada de mercadoria não depende do XML do fornecedor.
- Registrar, por venda, o líquido previsto de marketplace e conciliar o crédito efetivo pelo ID da venda com o extrato bancário.
- Fazer a conciliação com lançamentos manuais do extrato no primeiro recorte.
- Guardar o vídeo da embalagem por três meses, vinculado à venda.
- Manter os vídeos em armazenamento local persistente do servidor, com remoção automática após três meses.
- Acompanhar os serviços com logs centralizados, métricas e rastros no Grafana.
- Consultar operações e rastrear quem fez cada ação.

## Organização do projeto

O backend é o foco. O front-end será feito em React e ficará separado do backend. Python poderá entrar depois em uma integração ou tarefa que se beneficie dele, como análise de dados ou processamento em lote; não será criado um serviço Python sem uma necessidade de negócio definida.

Antes de implementar, vamos trabalhar em `docs/` para levantar processos, regras de negócio, modelo de domínio, diagramas e decisões técnicas. Os diagramas e regras iniciais serão hipóteses, validadas com exemplos da operação da loja.

Documentos de análise atuais:

- [Escopo e domínio](docs/01-escopo-e-dominio.md)
- [Modelo do domínio e fluxos](docs/02-modelo-do-dominio.md)
- [Cenários operacionais](docs/03-cenarios-operacionais.md)
- [Fronteiras dos microserviços](docs/04-fronteiras-de-servicos.md)
- [Observabilidade com Grafana](docs/05-observabilidade.md)

Estrutura pretendida, sujeita à decisão sobre monorepo e limites dos serviços:

```text
MF_Sistema/
├── docs/                  # processos, regras, diagramas e decisões
├── backend/               # serviços Java/Spring Boot
├── frontend/              # aplicação React
└── python/                # somente quando houver um caso de uso claro
```

## Arquitetura inicial

O sistema será dividido por capacidades de negócio. A divisão abaixo é uma hipótese de partida; vamos validá-la enquanto levantamos as regras da loja.

| Serviço | Responsabilidade principal |
| --- | --- |
| `discovery-server` | Registro e descoberta de serviços com Eureka. |
| `config-server` | Configuração externa e compartilhada dos serviços. |
| `api-gateway` | Entrada HTTP, roteamento e políticas comuns de API. |
| `rabbitmq` | Mensageria durável para eventos entre serviços, com retentativa e dead-letter queue. |
| `catalog-service` | Cadastro e consulta de produtos. |
| `inventory-service` | Estoque físico por caixa/posição, saldos e movimentações. |
| `receiving-service` | Registro, conferência e alocação de mercadoria recebida. |
| `sales-service` | Vendas online confirmadas pelo XML importado manualmente e vendas presenciais registradas no sistema. |
| `fulfillment-service` | Conferência, embalagem e registro da evidência do processo. |
| `finance-service` | Repasses de marketplace e conciliação com lançamentos do extrato bancário. |

Serviços terão limites claros e serão donos dos próprios dados. A Olist continua responsável pelos pedidos online, emissão de NF e etiqueta de marketplace; o sistema novo registra entradas manualmente e importa o XML de venda quando necessário para apoiar conferência, embalagem e financeiro. Chamadas síncronas atendem pesquisas/respostas imediatas; RabbitMQ distribui eventos duráveis de negócio entre serviços, com outbox, confirmação, retentativa e consumidores idempotentes.

## Princípios de implementação

- Java com Spring Boot e Maven; Lombok para reduzir código repetitivo quando melhorar a leitura.
- SOLID e padrões de projeto usados para resolver problemas concretos, sem criar abstrações sem necessidade.
- Sem setters nas classes de domínio. Mudanças serão expressas por métodos de negócio, por exemplo `confirmarRecebimento()` ou `reservar(...)`.
- Correções de dados serão ações explícitas do domínio, não setters genéricos. A entidade valida a alteração; o caso de uso coordena gravação e histórico; o repositório persiste cada responsabilidade separadamente.
- Imutabilidade sempre que fizer sentido. Entidades persistidas podem precisar de construtores e mapeamentos específicos do JPA; isso será tratado sem expor setters públicos.
- Herança será usada com parcimônia, quando houver uma relação “é um” estável. Composição será preferida quando representar melhor o domínio.
- APIs versionadas, validação de entrada, tratamento consistente de erros, logs e testes automatizados focados nas regras de negócio.
- Testes serão escritos e executados junto com cada fatia implementada: testes unitários para regras de domínio e testes de integração para APIs, persistência e mensageria quando esses componentes entrarem no fluxo.
- Docker e Docker Compose para executar a aplicação e suas dependências localmente.

## Caminho de construção

1. Entender a operação atual da loja e desenhar os fluxos e regras.
2. Definir o primeiro recorte funcional e os estados de cada processo.
3. Preparar estrutura Maven, convenções e execução local com Docker.
4. Implementar uma fatia vertical pequena, do endpoint até persistência e regra de negócio.
5. Cobrir e exercitar cenários reais com testes, como recebimento, reaproveitamento de posições vazias, estoque insuficiente, cancelamento e conciliação divergente.
6. Evoluir observabilidade, segurança e integração entre serviços conforme necessidade.

## Decisões ainda em aberto

- Detalhar os casos e efeitos de cancelamento/devolução ao longo do fluxo.
- Detalhar as ações permitidas dentro de cada módulo funcional, começando por acessos de Estoque, Financeiro e demais módulos do sistema.
- Definir o caminho do volume local exclusivo dos vídeos durante a implantação no servidor.

Essas respostas definem o primeiro fluxo que vamos implementar. A arquitetura acima é deliberadamente inicial e pode mudar para acompanhar as regras reais.
