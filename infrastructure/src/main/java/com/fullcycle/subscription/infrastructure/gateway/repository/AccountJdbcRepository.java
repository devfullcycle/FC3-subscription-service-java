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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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
    public Account save(final Account anAccount) {
        if (anAccount.version() == 0) {
            create(anAccount);
        } else {
            update(anAccount);
        }

        return anAccount;
    }

    private void create(final Account account) {
        final var sql = """
                INSERT INTO accounts (id, version, idp_user_id, email, firstname, lastname, document_number, document_type, address_zip_code, address_number, address_complement, address_country)
                VALUES (:id, (:version + 1), :userId, :email, :firstname, :lastname, :documentNumber, :documentType, :addressZipCode, :addressNumber, :addressComplement, :addressCountry)
                """;
        executeUpdate(sql, account);
    }

    private void update(final Account account) {
        final var sql = """
                UPDATE accounts
                SET
                    version = :version + 1,
                    idp_user_id = :userId,
                    email = :email,
                    firstname = :firstname,
                    lastname = :lastname,
                    document_number = :documentNumber,
                    document_type = :documentType,
                    address_zip_code = :addressZipCode,
                    address_number = :addressNumber,
                    address_complement = :addressComplement,
                    address_country = :addressCountry
                WHERE id = :id and version = :version
                """;

        if (executeUpdate(sql, account) == 0) {
            throw new IllegalArgumentException("Account with id %s and version %s was not found".formatted(account.id().value(), account.version()));
        }
    }

    private int executeUpdate(final String sql, final Account account) {
        final var params = new HashMap<String, Object>();
        params.put("version", account.version());
        params.put("userId", account.userId().value());
        params.put("email", account.email().value());
        params.put("firstname", account.name().firstname());
        params.put("lastname", account.name().lastname());
        params.put("documentNumber", account.document().value());
        params.put("documentType", account.document().type());

        final var address = account.billingAddress();
        params.put("addressZipCode", address != null ? address.zipcode() : "");
        params.put("addressNumber", address != null ? address.number() : "");
        params.put("addressComplement", address != null ? address.complement() : "");
        params.put("addressCountry", address != null ? address.country() : "");
        params.put("id", account.id().value());

        try {
            return this.jdbcClient.sql(sql).params(params).update();
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }

}
