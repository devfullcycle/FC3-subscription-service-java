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

## Keycloak

### Admin REST API

#### Authentication

Para usar a Admin REST API vamos precisar criar um *Client* que contenha permissões `manage-users`. 

**Passo 1: Criando client para o microserviço**
1. Ir até o realm **fc3-codeflix** (ou o nome que tenha escolhido).
2. Ir até o `Clients` -> `Create client`.
3. Preencher com as informações abaixo e clicar em `Next`:
   - **Client ID**: `fc3-subscription-service`
   - **Name**: `Subscription Service`
4. Nesse passo de `Capability config`, preencher com as informações abaixo e clicar em `Save`:
   - **Client authentication**: ON
   - **Authentication flow**: Habilitar `Standard flow`, `Direct access grants` e `Service accounts roles`.

**Passo 2: Configurando as permissões**
1. Ir até o `Clients` -> `fc3-subscription-service`.
2. Navegar até a aba `Service account roles` -> Botão `Assign role`.
3. Modificar o filtro para `Filter by clients`.
4. Selecionar `(realm-management) manage-users`.
5. Navegar até a aba `Advanced` -> Habilitar `Use refresh tokens for client credentials grant` e salvar.

Feito isso podemos utilizar esse user para gerenciar os novos usuários e suas roles.

Login URL: http://keycloak.internal:8443/realms/fc3-codeflix/protocol/openid-connect/auth?response_type=code&client_id=fc3-subscription-service-cli&redirect_uri=http://localhost:8080/api/authorization-callback