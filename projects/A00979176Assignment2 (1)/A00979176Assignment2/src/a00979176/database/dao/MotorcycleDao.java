/**
 * Project: A00979176Assignment2
 * File: MotorcycleDao.java
 * Date: July 1, 2017
 */

package a00979176.database.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00979176.ApplicationException;
import a00979176.data.Motorcycle;
import a00979176.database.Database;
import a00979176.database.util.DbConstants;
import a00979176.io.MotorcycleReader;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class MotorcycleDao extends Dao {

	public static final String TABLE_NAME = DbConstants.MOTORCYCLE_TABLE_NAME;

	private static Logger LOG = LogManager.getLogger();
	private static final String INVENTORY_DATA_FILE = "motorcycles.dat";

	public MotorcycleDao(Database database) throws ApplicationException {
		super(database, TABLE_NAME);
		LOG.debug("Constructor MotorcycleDao()");
		load(new File(INVENTORY_DATA_FILE));
	}

	public void load(File motorcycleData) throws ApplicationException {
		LOG.debug("MotorcycleDao load()");
		try {
			if (!Database.tableExists(MotorcycleDao.TABLE_NAME) || Database.dbTableDropRequested()) {
				if (Database.tableExists(MotorcycleDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}
				create();
				LOG.debug("Inserting the inventories");
				if (!motorcycleData.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", INVENTORY_DATA_FILE));
				}
				MotorcycleReader.read(motorcycleData, this);
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			throw new ApplicationException(e);
		}
	}

	@Override
	public void create() throws SQLException {
		LOG.debug("Creating database table " + TABLE_NAME);
		String sqlString = String.format(
				"CREATE TABLE %s(" + "%s BIGINT, " + "%s VARCHAR(%d), " + "%s VARCHAR(%d), " + "%s BIGINT, "
						+ "%s VARCHAR(%d), " + "%s BIGINT, " + "%s BIGINT, " + "PRIMARY KEY (%s))",
				TABLE_NAME, Column.ID.name, Column.MAKE.name, Column.MAKE.length, Column.MODEL.name,
				Column.MODEL.length, Column.YEAR_MADE.name, Column.SERIAL_NUMBER.name, Column.SERIAL_NUMBER.length,
				Column.MILEAGE.name, Column.CUSTOMER_ID.name, Column.ID.name);
		super.create(sqlString);
	}

	public void add(Motorcycle motorcycle) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		execute(sqlString,
				motorcycle.getId(),
				motorcycle.getMake(),
				motorcycle.getModel(),
				motorcycle.getYear(),
				motorcycle.getSerialNumber(),
				motorcycle.getMileage(),
				motorcycle.getCustomerId());
	}

	public void update(Motorcycle motorcycle) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %d=?", TABLE_NAME,
				Column.MAKE.name,
				Column.MODEL.name,
				Column.YEAR_MADE.name,
				Column.SERIAL_NUMBER.name,
				Column.MILEAGE.name,
				Column.CUSTOMER_ID.name,
				Column.ID.name);
		LOG.debug("Update statement: " + sqlString);
		
		execute(sqlString, motorcycle.getMake(), motorcycle.getModel(),
				motorcycle.getYear(), motorcycle.getSerialNumber(), motorcycle.getMileage(),
				motorcycle.getCustomerId(), motorcycle.getId());
	}

	public void delete(Motorcycle motorcycle) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s=%s", TABLE_NAME, Column.ID.name,
					motorcycle.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public List<Long> getMotorcycleIds() throws SQLException {
		List<Long> ids = new ArrayList<>();
		String selectString = String.format("SELECT %s FROM %s", Column.ID.name, TABLE_NAME);
		LOG.debug(selectString);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				ids.add(resultSet.getLong(Column.ID.name));
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("Loaded %d motorcycle ID(s) from the database", ids.size()));

		return ids;
	}

	public List<Motorcycle> getAllMotorcycles() throws SQLException {
		List<Motorcycle> motorcycle = new ArrayList<>();
		String selectString = String.format("SELECT * FROM %s", TABLE_NAME);
		LOG.debug(selectString);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				Motorcycle mot = new Motorcycle.Builder(resultSet.getInt(Column.ID.name)) //
						.setMake(resultSet.getString(Column.MAKE.name)) //
						.setModel(resultSet.getString(Column.MODEL.name)) //
						.setYear(resultSet.getInt(Column.YEAR_MADE.name))
						.setSerialNumber(resultSet.getString(Column.SERIAL_NUMBER.name))
						.setMileage(resultSet.getInt(Column.MILEAGE.name))
						.setCustomerId(resultSet.getLong(Column.CUSTOMER_ID.name)).build();
				motorcycle.add(mot);
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("Loaded %d register(s) from the database", motorcycle.size()));

		return motorcycle;
	}

	public Motorcycle getMotorcycle(Long motorcycleId) throws SQLException, Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, motorcycleId);
		LOG.debug(sqlString);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}
				Motorcycle motorcycle = new Motorcycle.Builder(resultSet.getInt(Column.ID.name)) //
						.setMake(resultSet.getString(Column.MAKE.name)) //
						.setModel(resultSet.getString(Column.MODEL.name)) //
						.setYear(resultSet.getInt(Column.YEAR_MADE.name))
						.setSerialNumber(resultSet.getString(Column.SERIAL_NUMBER.name))
						.setMileage(resultSet.getInt(Column.MILEAGE.name))
						.setCustomerId(resultSet.getLong(Column.CUSTOMER_ID.name)).build();
				return motorcycle;
			}
		} finally {
			close(statement);
		}
		return null;
	}

	public int countAllMotorcycles() throws SQLException, Exception {
		Statement statement = null;
		int count = 0;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT COUNT(*) AS total FROM %s", tableName);
			ResultSet resultSet = statement.executeQuery(sqlString);
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}
		} finally {
			close(statement);
		}
		return count;
	}

	public static void init() throws ApplicationException {
		try {
			throw new SQLException();
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public enum Column {
		ID("id", 6), //
		MAKE("make", 25), //
		MODEL("model", 25), //
		YEAR_MADE("yearMade", 10), //
		SERIAL_NUMBER("serialNumber", 25), //
		MILEAGE("mileage", 10), //
		CUSTOMER_ID("customerId", 10);

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}
	}
}
