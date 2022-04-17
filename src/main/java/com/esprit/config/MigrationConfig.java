package com.esprit.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

@Configuration
@ConditionalOnProperty(name = "application.migration.enable", havingValue = "true", matchIfMissing = true)
public class MigrationConfig {

	@Bean(name = "scoemploi")
	public DataSource dataSourceScoemploi() throws SQLException {
		PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
		dataSource.setUser("scoemploi");
		dataSource.setPassword("1234");
		dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
		dataSource.setURL("jdbc:oracle:thin:@localhost:1529:xe");
		return dataSource;
	}

	@Bean(name = "scoemploiJdbc")
	public JdbcTemplate jdbcTemplateScoemploi(@Qualifier("scoemploi") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}