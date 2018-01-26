package br.com.cafebinario.transactiontest.configuration;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(transactionManagerRef = "principalTransactionManager", entityManagerFactoryRef = "principalEntityManagerFactory", basePackages = {
		"br.com.cafebinario.transactiontest.principal.repositories" })
public class DatabasePrincipalConfiguration {
	
	private static final String PU_NAME = "principalPersistenceUnitName";

	private static final String PACKAGE_SCAN = "br.com.cafebinario.transactiontest.pricipal.entities";

	@Value("${principal.datasource.username}")
	private String username;

	@Value("${principal.datasource.password}")
	private String password;

	@Value("${principal.datasource.url}")
	private String url;

	@Value("${principal.datasource.driverClassName}")
	private String driver;

	@Value("${principal.datasource.dialect}")
	private String dialect;

	@Value("${principal.datasource.showSql:false}")
	private Boolean showSql;
	
	@Value("${principal.jpa.generateDdl:create}")
	private String generateDDl;


	@Primary
	@Bean(name = "principalEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean principalEntityManagerFactory() throws SQLException {
		final Properties properties = principalProperties();
		final DataSource dataSource = principalDataSource();
		return DatabaseConfigurationHelper.entityManagerFactory(dataSource, properties, PACKAGE_SCAN, PU_NAME);
	}

	@Primary
	@Bean("principalTransactionManager")
	public PlatformTransactionManager principalTransactionManager() throws SQLException {
		return DatabaseConfigurationHelper.platformTransactionManager(principalEntityManagerFactory().getObject());
	}

	@Primary
	@Bean("principalDataSource")
	public DataSource principalDataSource() throws SQLException {
		final Properties properties = principalProperties();
		return DatabaseConfigurationHelper.dataSource(username, password, url, driver, "principalDs", properties);
	}

	@Bean("principalProperties")
	public Properties principalProperties() {
		final Properties properties = DatabaseConfigurationHelper.properties(dialect);
		properties.put("hibernate.hbm2ddl.auto", generateDDl);
		properties.put("spring.jpa.show-sql", String.valueOf(showSql));

		return properties;
	}
}
