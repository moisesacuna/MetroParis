package it.polito.tdp.metroparis.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Utility class for connecting to the database
 * 
 * Uses the HikariCP library for managing a connection pool
 * @see <a href="https://brettwooldridge.github.io/HikariCP/">HikariCP</a>
 */
public class DBConnect {

	private static final String jdbcURL = "jdbc:postgresql://192.168.64.18:5433/polito";	
	private static HikariDataSource ds;

	public static Connection getConnection() {

		if (ds == null) {
			
			ds = new HikariDataSource();
			ds.setJdbcUrl(jdbcURL);
			ds.setUsername("webpolito");
			ds.setPassword("ravenna");
			// configurazione POSTGRESQL
			ds.addDataSourceProperty("validationQuery", "SELECT 1");
			ds.addDataSourceProperty("cachePrepStmts", "true");
			ds.addDataSourceProperty("prepStmtCacheSize", "250");
			ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			ds.addDataSourceProperty("initialSize", "0");
			ds.addDataSourceProperty("maxActive", "80");
			ds.addDataSourceProperty("maxIdle", "30");
			ds.addDataSourceProperty("minIdle", "10");
			ds.addDataSourceProperty("timeBetweenEvictionRunsMillis", "30000");  
			ds.addDataSourceProperty("minEvictableIdleTimeMillis", "60000");  
			ds.addDataSourceProperty("testOnBorrow", "30000");  
			ds.addDataSourceProperty("timeBetweenEvictionRunsMillis", "30000");  
			ds.addDataSourceProperty("validationInterval", "30000");
			ds.addDataSourceProperty("removeAbandoned", "true");   
			ds.addDataSourceProperty("removeAbandonedTimeout", "120"); 
			ds.addDataSourceProperty("logAbandoned", "true");
			ds.addDataSourceProperty("abandonWhenPercentageFull", "60");

		}

		try {

			return ds.getConnection();

		} catch (SQLException e) {
			System.err.println("Errore connessione al DB");
			throw new RuntimeException(e);
		}
	}

}
