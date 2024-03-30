# Clean Architecture Gateways

## Tipos de implementações de Gateways

1. `Clients`: São gateways HTTP que dão acesso a um sistema externo. Esses gateways possuem o sufixo `Client` quando
   o mesmo foi criado exclusivamente para implementar o Gateway e recebe como parametro o Client HTTP baixo nível.
   Nos casos onde utiliza-se um outra classe client para se conectar com um serviço externo e esse gateway é apenas
   para converter a resposta desse client para a interface do gateway, leva-se o sufixo `ClientAdapter`.
2. `Repositories`: São gateways que dão acesso a camada de persistência da aplicação, independente se é um RDBMS ou um
   NoSQL. Esses gateways recebem o sufixo `Repository` quando o mesmo foi criado exclusivamente para implementar o
   Gateway e recebe como parametro o JDBC client baixo nível. Nos casos onde utiliza-se uma outra classe, seja um
   Repository do Spring Data ou um ActiveRecord do Panache por exemplo, leva-se o sufixo `RepositoryAdapter`.