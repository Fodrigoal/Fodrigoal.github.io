/**
 * Project: A00979176Assignment2
 * File: Database.java
 * Date: July 1, 2017
 */
package a00979176.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class Database {

	public static final String DB_DRIVER_KEY = "db.driver";
	public static final String DB_URL_KEY = "db.url";
	public static final String DB_USER_KEY = "db.user";
	public static final String DB_PASSWORD_KEY = "db.password";

	private static final Logger LOG = LogManager.getLogger();
	private static Connection connection;
	private static Properties properties;
	private static boolean dbTableDropRequested;

	public Database(Properties properties) throws FileNotFoundException, IOException {
		LOG.debug("Loading database properties from db.properties");
		Database.properties = properties;
	}

	public static Connection getConnection() throws SQLException {
		if (connection != null) {
			return connection;
		}
		try {
			connect();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		return connection;
	}

	private static void connect() throws ClassNotFoundException, SQLException {
		String dbDriver = properties.getProperty(DB_DRIVER_KEY);
		LOG.debug(dbDriver);
		Class.forName(dbDriver);
		LOG.debug("Driver loaded");
		connection = DriverManager.getConnection(properties.getProperty(DB_URL_KEY),
				properties.getProperty(DB_USER_KEY), properties.getProperty(DB_PASSWORD_KEY));
		LOG.debug("Database connected");

		if (dbTableDropRequested) {
		}
	}

	public void shutdown() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}
	}

	public static boolean tableExists(String targetTableName) throws SQLException {
		DatabaseMetaData dbMetaData = getConnection().getMetaData();
		ResultSet resultSet = null;
		String tableName = null;
		try {
			resultSet = dbMetaData.getTables(connection.getCatalog(), "%", "%", null);
			while (resultSet.next()) {
				tableName = resultSet.getString("TABLE_NAME");
				if (tableName.equalsIgnoreCase(targetTableName)) {
					LOG.debug("Found the target table named: " + targetTableName);
					return true;
				}
			}
		} finally {
			resultSet.close();
		}
		return false;
	}

	public static void requestDbTableDrop() {
		dbTableDropRequested = true;
	}

	public static boolean dbTableDropRequested() {
		return dbTableDropRequested;
	}
}
