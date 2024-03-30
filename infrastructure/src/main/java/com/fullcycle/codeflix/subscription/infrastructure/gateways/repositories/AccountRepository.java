package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories;

import com.fullcycle.codeflix.subscription.domain.account.Account;
import com.fullcycle.codeflix.subscription.domain.account.AccountGateway;
import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.person.Address;
import com.fullcycle.codeflix.subscription.domain.person.Document;
import com.fullcycle.codeflix.subscription.domain.person.Email;
import com.fullcycle.codeflix.subscription.domain.person.Name;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.infrastructure.database.DatabaseClient;
import com.fullcycle.codeflix.subscription.infrastructure.database.RowMapping;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AccountRepository implements AccountGateway {

    private static final RowMapping<Account> accountRowMapping = userRowMapping();

    private final DatabaseClient database;
    private final EventRepository eventRepository;

    public AccountRepository(final DatabaseClient database, final EventRepository eventRepository) {
        this.database = Objects.requireNonNull(database);
        this.eventRepository = Objects.requireNonNull(eventRepository);
    }

    @Override
    public AccountId nextId() {
        return new AccountId(IdUtils.uniqueId());
    }

    @Override
    public List<Account> allAccounts() {
        final var sql = "SELECT id, version, email, firstname, lastname, address_zip_code, address_number, address_complement, address_country, document_number, document_type FROM accounts";
        return this.database.query(sql, Map.of(), accountRowMapping);
    }

    @Override
    public Optional<Account> accountOfId(final AccountId accountId) {
        final var sql = "SELECT id, version, email, firstname, lastname, address_zip_code, address_number, address_complement, address_country, document_number, document_type FROM accounts WHERE id = :id";
        final var rows = this.database.query(sql, Map.of("id", accountId.value()), accountRowMapping);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rows.getFirst());
    }

    @Override
    public Account save(final Account account) {
        if (account.version() == 0) {
            create(account);
        } else {
            update(account);
        }

        this.eventRepository.saveAll(account.domainEvents());
        return account;
    }

    private Account create(final Account user) {
        final var sql = """
                INSERT INTO accounts (id, version, email, firstname, lastname, address_zip_code, address_number, address_complement, address_country, document_number, document_type)
                VALUES (:id, (:version + 1), :email, :firstname, :lastname, :address_zip_code, :address_number, :address_complement, :address_country, :document_number, :document_type)
                """;
        executeUpdate(sql, user);
        return user;
    }

    private Account update(final Account user) {
        final var sql = """
                UPDATE accounts
                SET
                    version = (:version + 1),
                    email = :email,
                    firstname = :firstname,
                    lastname = :lastname,
                    address_zip_code = :address_zip_code,
                    address_number = :address_number,
                    address_complement = :address_complement,
                    address_country = :address_country,
                    document_number = :document_number,
                    document_type = :document_type
                WHERE id = :id AND version = :version
                """;

        if (executeUpdate(sql, user) == 0) {
            throw new IllegalStateException("0 rows affected");
        }

        return user;
    }

    private int executeUpdate(final String sql, final Account user) {
        final var params = new HashMap<String, Object>();
        params.put("id", user.id().value());
        params.put("version", user.version());
        params.put("email", user.email().value());
        params.put("firstname", user.name().firstname());
        params.put("lastname", user.name().lastname());
        params.put("document_number", user.document().value());
        params.put("document_type", user.document().type());

        final var address = user.billingAddress();
        params.put("address_zip_code", address != null ? address.zipcode() : null);
        params.put("address_number", address != null ? address.number() : null);
        params.put("address_complement", address != null ? address.complement() : null);
        params.put("address_country", address != null ? address.country() : null);

        try {
            return this.database.update(sql, params);
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }

    private static RowMapping<Account> userRowMapping() {
        return rs -> Account.with(
                new AccountId(rs.getString("id")),
                rs.getInt("version"),
                new Email(rs.getString("email")),
                new Name(rs.getString("firstname"), rs.getString("lastname")),
                Document.create(
                        rs.getString("document_number"),
                        rs.getString("document_type")
                ),
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
}
