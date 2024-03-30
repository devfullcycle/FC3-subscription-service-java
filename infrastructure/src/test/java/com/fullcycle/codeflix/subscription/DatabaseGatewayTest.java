package com.fullcycle.codeflix.subscription;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.lang.annotation.*;

/**
 * Anotação que pode ser utilizada para testes integrados <strong>exclusivos</strong> de componentes Data JDBCs.
 * <p>
 * Utilizar essa anotação irá desabilitar a auto-configuração completa e escanear somente
 * {@code AbstractJdbcConfiguration} subclasses e aplicará somente configuração pertinente aos testes Data JDBC.
 * <p>
 * <p>
 * Por padrão, os testes anotados com {@code @DatabaseGatewayTest} são transacionais e o roll back é feito automaticamente
 * ao final de cada teste, contanto que compartilhe da mesma transação criada pelo teste. Caso, por exemplo, esteja modificando
 * o escopo transacional via {@code @Transactional(propagation = REQUIRES_NEW)}, o mesmo será commitado ao final dessa transação
 * e seu roll back não é feito automaticamente.
 * </p>
 * <p>
 * Além disso, essa anotação também já configura por padrão um banco de dados embedded em memória, substituindo qualquer
 * configuração explícita. A {@link AutoConfigureTestDatabase @AutoConfigureTestDatabase} pode ser usada para customizar isso.
 * </p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@DataJdbcTest
@Tag("integrationTest")
public @interface DatabaseGatewayTest {
}
