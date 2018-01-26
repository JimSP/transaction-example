package br.com.cafebinario.transactiontest.configuration;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(transactionManagerRef = "secundaryTransactionManager", entityManagerFactoryRef = "secundaryEntityManagerFactory", basePackages = {
		"br.com.cafebinario.transactiontest.secundary.repositories" })
public class DatabaseSecundaryConfiguration {
	private static final String PU_NAME = "secundaryPersistenceUnitName";
	private static final String PACKAGE_SCAN = "br.com.cafebinario.transactiontest.secundary.entities";

	@Value("${secundary.datasource.username}")
	private String username;

	@Value("${secundary.datasource.password}")
	private String password;

	@Value("${secundary.datasource.url}")
	private String url;

	@Value("${secundary.datasource.driverClassName}")
	private String driver;

	@Value("${secundary.datasource.dialect}")
	private String dialect;

	@Value("${secundary.datasource.showSql:false}")
	private Boolean showSql;

	@Value("${secundary.jpa.generateDdl:create}")
	private String generateDDl;

	@Bean(name = "secundaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean principalEntityManagerFactory() throws SQLException {
		final Properties properties = secundaryProperties();
		final DataSource dataSource = secundaryDataSource();
		return DatabaseConfigurationHelper.entityManagerFactory(dataSource, properties, PACKAGE_SCAN, PU_NAME);
	}

	@Bean("secundaryTransactionManager")
	public PlatformTransactionManager principalTransactionManager() throws SQLException {
		return DatabaseConfigurationHelper.platformTransactionManager(principalEntityManagerFactory().getObject());
	}

	@Bean("secundaryDataSource")
	public DataSource secundaryDataSource() throws SQLException {
		final Properties properties = secundaryProperties();
		return DatabaseConfigurationHelper.dataSource(username, password, url, driver, "dbSecundaryXaDs", properties);
	}

	@Bean("secundaryProperties")
	public Properties secundaryProperties() {
		final Properties properties = DatabaseConfigurationHelper.properties(dialect);
		properties.put("hibernate.hbm2ddl.auto", generateDDl);
		properties.put("spring.jpa.show-sql", String.valueOf(showSql));

		return properties;
	}
}
