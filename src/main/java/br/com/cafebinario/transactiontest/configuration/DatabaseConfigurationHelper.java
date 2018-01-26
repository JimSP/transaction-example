package br.com.cafebinario.transactiontest.configuration;

import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.apache.tomcat.jdbc.pool.XADataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

public final class DatabaseConfigurationHelper {

	private static final String DIALECT = "hibernate.dialect";

	private DatabaseConfigurationHelper() {
	}

	protected static Properties properties(final String dialect) {
		final Properties properties = new Properties();
		properties.setProperty(DIALECT, dialect);
		return properties;
	}

	protected static DataSource dataSource(final String username, final String password, final String url,
			final String driverClassName, final String uniqueResourceName, final Properties properties)
			throws SQLException {
		final XADataSource xaDataSource = new XADataSource();
		xaDataSource.setUsername(username);
		xaDataSource.setPassword(password);
		xaDataSource.setUrl(url);
		xaDataSource.setDriverClassName(driverClassName);
		xaDataSource.setDbProperties(properties);
		xaDataSource.setMaxActive(10);
		xaDataSource.setDefaultAutoCommit(Boolean.FALSE);
		xaDataSource.setJmxEnabled(Boolean.TRUE);

		return xaDataSource;
	}

	protected static LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource,
			final Properties properties, final String packageScan, final String puName) throws SQLException {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		final HibernateJpaDialect hibernateJpaDialect = new HibernateJpaDialect();

		em.setPackagesToScan(packageScan);
		em.setDataSource(dataSource);
		em.setJpaVendorAdapter(vendorAdapter);
		em.setPersistenceUnitName(puName);
		em.setJpaDialect(hibernateJpaDialect);
		em.setJpaProperties(properties);

		return em;
	}

	protected static JtaTransactionManager jtaTransactionManager(final TransactionManager transactionManager)
			throws SQLException {
		return new JtaTransactionManager(transactionManager);
	}

	protected static PlatformTransactionManager platformTransactionManager(
			final EntityManagerFactory entityManagerFactory) throws SQLException {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
