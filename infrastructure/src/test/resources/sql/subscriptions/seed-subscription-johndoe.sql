insert into plans (id, version, name, description, active, currency, amount, created_at, updated_at, deleted_at)
values (1, 1, 'Master', 'O plano mais custo benefício', false, 'BRL', 20, '2024-04-28 10:57:11.111', '2024-04-28 10:58:11.111', '2024-04-28 10:59:11.111');

insert into accounts (id, version, idp_user_id, email, firstname, lastname, document_number, document_type, address_zip_code, address_number, address_complement, address_country)
values ('033c7d9eb3cc4eb7840b942fa2194cab', 1, 'a8b3cf5a-5f81-4822-9ee8-89e768f6095c', 'john@gmail.com', 'John', 'Doe', '12312312332', 'cpf', '12332123', '1', 'Casa 1', 'BR');

insert into subscriptions (id, version, account_id, plan_id, status, created_at, updated_at, due_date, last_renew_dt, last_transaction_id)
values ('5783bdbcbb2347eb8883e969f14d350c', 1, '033c7d9eb3cc4eb7840b942fa2194cab', 1, 'active', '2024-04-28 10:58:11.111', '2024-04-28 10:59:11.111', '2024-05-27', '2024-04-27 10:59:11.111', '560f4e6a-79fa-473c-b7cb-a5b2bb4e6c8a')