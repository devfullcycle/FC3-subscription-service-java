
# Clean Architecture Gateways

## Tipos de implementações de Gateways

1. `Adapters`: É um caso particular de implementação onde não conseguimos customizar o _driven actor_ de tal forma que
    conseguimos fazer a inversão de dependência, nos obrigando a criar esse "middle man" para fazer a adaptação de 
    um para outro e vice-versa. Para tangibilizar melhor a explicação, podemos pensar no Spring Data que nos obriga
    a utilizar o *Repository pattern* para que ele implemente a camada de persistência. Não tem como fugir da interface
    `XptoRepository`, logo, precisamos criar um `XptoRepositoryAdapter` para fazer a adaptação.
2. `Client Https`: Nesse cenário não é necessário criar um adapter, podemos fazer a implementação do Gateway de domínio
    ser o próprio componente *client* que conhece do _driven actor_.
3. `In memory`: tbd
4. `Filesystem`: tbd
5. `Configurations` tbd