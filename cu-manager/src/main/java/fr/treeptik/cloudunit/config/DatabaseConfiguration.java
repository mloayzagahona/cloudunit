/*
 * LICENCE : CloudUnit is available under the Gnu Public License GPL V3 : https://www.gnu.org/licenses/gpl.txt
 *     but CloudUnit is licensed too under a standard commercial license.
 *     Please contact our sales team if you would like to discuss the specifics of our Enterprise license.
 *     If you are not sure whether the GPL is right for you,
 *     you can always test our software under the GPL and inspect the source code before you contact us
 *     about purchasing a commercial license.
 *
 *     LEGAL TERMS : "CloudUnit" is a registered trademark of Treeptik and can't be used to endorse
 *     or promote products derived from this project without prior written permission from Treeptik.
 *     Products or services derived from this software may not be called "CloudUnit"
 *     nor may "Treeptik" or similar confusing terms appear in their names without prior written permission.
 *     For any questions, contact us : contact@treeptik.fr
 */

package fr.treeptik.cloudunit.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.ejb.HibernatePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("fr.treeptik.cloudunit.dao")
@EnableTransactionManagement
public class DatabaseConfiguration {

	private Logger logger = LoggerFactory
			.getLogger(DatabaseConfiguration.class);

	@Inject
	private Environment env;

	@Value("classpath:/${database.script}")
	private Resource dataScript;

	@Bean
	public DataSource dataSource() {
		logger.debug("Configuring Datasource");
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		if (env.getProperty("database.url") == null
				|| "".equals(env.getProperty("database.url"))) {
			config.addDataSourceProperty("databaseName",
					env.getProperty("cloudunit"));
		} else {
			config.addDataSourceProperty("url", env.getProperty("database.url"));
		}
		config.addDataSourceProperty("user", env.getProperty("database.user"));
		config.addDataSourceProperty("password",
				env.getProperty("database.password"));
		// config.setAutoCommit(false);
		return new HikariDataSource(config);
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(
			final DataSource dataSource) {
		final DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(databasePopulator());
		return initializer;
	}

	private DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.setSqlScriptEncoding("utf-8");
		populator.addScript(dataScript);
		return populator;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		logger.debug("Configuring EntityManager");
		LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
		lcemfb.setPersistenceProvider(new HibernatePersistence());
		lcemfb.setPersistenceUnitName("persistenceUnit");
		lcemfb.setDataSource(dataSource());
		lcemfb.setJpaDialect(new HibernateJpaDialect());
		lcemfb.setJpaVendorAdapter(jpaVendorAdapter());
		lcemfb.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.generate_statistics", true);
		jpaProperties.put("hibernate.show_sql", false);
		lcemfb.setJpaProperties(jpaProperties);
		lcemfb.setPackagesToScan("fr.treeptik.cloudunit.model");
		lcemfb.afterPropertiesSet();
		return lcemfb.getObject();
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setShowSql(false);
		jpaVendorAdapter.setGenerateDdl(true);
		jpaVendorAdapter
				.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
		return jpaVendorAdapter;
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory());
		return jpaTransactionManager;
	}
}