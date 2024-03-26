<center>
  <p align="center">
    <img src="https://user-images.githubusercontent.com/20674439/158480514-a529b310-bc19-46a5-ac95-fddcfa4776ee.png" width="150"/>&nbsp;
    <img src="https://icon-library.com/images/java-icon-png/java-icon-png-15.jpg"  width="150" />
  </p>  
  <h1 align="center">🚀 Microserviço: Assinatura</h1>
  <p align="center">
    Microserviço referente ao backend do Catálogo de Vídeos<br />
    Utilizando Clean Architecture, TDD e as boas práticas atuais de mercado
  </p>
</center>
<br />

## Ferramentas necessárias

- JDK 21
- IDE de sua preferência
- Docker

## Fluxos

### Detalhes da criação de um usuário com vínculo no Keycloak
```mermaid
flowchart TD
    A[Sign up] -->|input data| B(SignUp mediator)
    B -->|input data| C(IAM SignUp - Keycloak)
    B -->|input data with iam id| D(UserAccount SignUp)
```

### Ciclo de vida de uma Subscription
```mermaid
flowchart TD
    A[Create Subscription] --> B(Trial)
    B -- Charge Subscription --> C{charge succeeded?}
    C -->|YES| D[Active]
    C -->|CANCEL| F[Canceled]
    C -->|NO| E[Incomplete]
    E --> |Retry charging| B
```
