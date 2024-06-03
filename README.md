
# Order Management API

## Descrição
API para gestão de pedidos utilizando Spring Boot e MySQL.

## Requisitos
- Java 1.8 ou superior
- MySQL
- Maven

## Configuração

1. Clone o repositório:
   ```sh
   git clone https://github.com/seu-usuario/order-management-api.git
Crie o banco de dados:
 - Utilizar o arquivo banco.sql


Copiar código
 - mvn spring-boot:run

Extremidade
 - POST /api/orders: Criação de um novo pedido.
 - GET /api/orders: Consulta de todos os pedidos.
 - GET /api/orders/{controlNumber}: Consulta de pedidos por número de controle.