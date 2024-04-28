package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.domain.account.Account;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.account.idp.UserId;
import com.fullcycle.subscription.domain.person.Address;
import com.fullcycle.subscription.domain.person.Document;
import com.fullcycle.subscription.domain.person.Email;
import com.fullcycle.subscription.domain.person.Name;
import com.fullcycle.subscription.domain.utils.IdUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class AccountJdbcRepository implements AccountGateway {

    private final JdbcClient jdbcClient;

    public AccountJdbcRepository(final JdbcClient jdbcClient) {
        this.jdbcClient = Objects.requireNonNull(jdbcClient);
    }

    @Override
    public AccountId nextId() {
        return new AccountId(IdUtils.uniqueId());
    }

    @Override
    public Optional<Account> accountOfId(final AccountId anId) {
        final var sql = """
                SELECT
                    id, version, idp_user_id, email, firstname, lastname, document_number, document_type, address_zip_code, address_number, address_complement, address_country
                FROM accounts
                WHERE id = :id
                """;
        return this.jdbcClient.sql(sql).param("id", anId.value()).query(accountMapper()).optional();
    }

    private RowMapper<Account> accountMapper() {
        return (rs, rowNumber) -> Account.with(
                new AccountId(rs.getString("id")),
                rs.getInt("version"),
                new UserId(rs.getString("idp_user_id")),
                new Email(rs.getString("email")),
                new Name(rs.getString("firstname"), rs.getString("lastname")),
                Document.create(rs.getString("document_number"), rs.getString("document_type")),
                rs.getString("address_zip_code") != null ?
                        new Address(
                                rs.getString("address_zip_code"),
                                rs.getString("address_number"),
                                rs.getString("address_complement"),
                                rs.getString("address_country")
                        ) :
                        null
        );
    }

    @Override
    public Optional<Account> accountOfUserId(UserId userId) {
        return Optional.empty();
    }

    @Override
    public Account save(Account anAccount) {
        return null;
    }
}
