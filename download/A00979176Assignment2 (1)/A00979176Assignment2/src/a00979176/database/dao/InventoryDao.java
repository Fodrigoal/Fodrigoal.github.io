/**
 * Project: A00979176Assignment2
 * File: InventoryDao.java
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
import a00979176.data.Inventory;
import a00979176.database.Database;
import a00979176.database.util.DbConstants;
import a00979176.io.InventoryReader;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class InventoryDao extends Dao {

	public static final String TABLE_NAME = DbConstants.INVENTORY_TABLE_NAME;
	private static Logger LOG = LogManager.getLogger();
	private static final String INVENTORY_DATA_FILE = "inventory.dat";

	public InventoryDao(Database database) throws ApplicationException {
		super(database, TABLE_NAME);
		load(new File(INVENTORY_DATA_FILE));
	}

	public void load(File inventoryData) throws ApplicationException {
		LOG.debug("Inventory load()");
		try {
			if (!Database.tableExists(InventoryDao.TABLE_NAME) || Database.dbTableDropRequested()) {
				if (Database.tableExists(InventoryDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}
				create();

				LOG.debug("Inserting the parts (inventory)");
				if (!inventoryData.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", INVENTORY_DATA_FILE));
				}
				InventoryReader.read(inventoryData, this);
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
				"CREATE TABLE %s(" + "%s VARCHAR(%d), " + "%s VARCHAR(%d), " + "%s BIGINT, " + "%s BIGINT, "
						+ "%s VARCHAR(%d), " + "PRIMARY KEY(%s))",
				TABLE_NAME, // 1
				Column.PART_NUMBER.name, Column.PART_NUMBER.length, Column.DESCRIPTION.name, Column.DESCRIPTION.length,
				Column.PRICE.name, Column.QUANTITY.name, Column.MOTORCYCLE_ID.name, Column.MOTORCYCLE_ID.length,
				Column.PART_NUMBER.name);
		super.create(sqlString);
	}

	public void add(Inventory inventory) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				inventory.getPartNumber(), //
				inventory.getDescription(), //
				inventory.getPrice(), //
				inventory.getQuantity(), //
				inventory.getMotorcycleId());
		LOG.debug(String.format("Adding %s was %s", inventory, result ? "successful" : "unsuccessful"));
	}

	public void update(Inventory inventory) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?", //
				TABLE_NAME, //
				Column.DESCRIPTION.name, //
				Column.PRICE.name, //
				Column.QUANTITY.name, //
				Column.MOTORCYCLE_ID.name, //
				Column.PART_NUMBER.name);
		LOG.debug("Update statment: " + sqlString);
		boolean result = execute(sqlString, inventory.getDescription(), inventory.getPrice(), inventory.getQuantity(),
				inventory.getMotorcycleId(), inventory.getPartNumber());
		LOG.debug(String.format("Updating %s was %s", inventory, result ? "successful" : "unsuccessful"));
	}

	public void delete(Inventory inventory) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();
			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.PART_NUMBER.name,
					inventory.getPartNumber());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public List<String> getInventoryIds() throws SQLException {
		List<String> ids = new ArrayList<>();
		String selectString = String.format("SELECT %s FROM %s", Column.PART_NUMBER.name, TABLE_NAME);
		LOG.debug(selectString);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				ids.add(resultSet.getString(Column.PART_NUMBER.name));
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("Loaded %d inventory ID(s) from the database", ids.size()));

		return ids;
	}

	public List<Inventory> getAllInventory() throws SQLException {
		List<Inventory> inventory = new ArrayList<>();
		String selectString = String.format("SELECT * FROM %s", TABLE_NAME);
		LOG.debug(selectString);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				Inventory inv = new Inventory.Builder(resultSet.getString(Column.PART_NUMBER.name)) //
						.setDescription(resultSet.getString(Column.DESCRIPTION.name)) //
						.setPrice(resultSet.getInt(Column.PRICE.name)) //
						.setQuantity(resultSet.getInt(Column.QUANTITY.name))
						.setMotorcycleId(resultSet.getString(Column.MOTORCYCLE_ID.name)).build();
				inventory.add(inv);
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("Loaded %d register(s) from the database", inventory.size()));

		return inventory;
	}

	public Inventory getInventory(String inventoryPartNumber) throws SQLException, Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, Column.PART_NUMBER.name,
				inventoryPartNumber);
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
				Inventory inventory = new Inventory.Builder(resultSet.getString(Column.PART_NUMBER.name)) //
						.setDescription(resultSet.getString(Column.DESCRIPTION.name)) //
						.setPrice(resultSet.getInt(Column.PRICE.name)) //
						.setQuantity(resultSet.getInt(Column.QUANTITY.name))
						.setMotorcycleId(resultSet.getString(Column.MOTORCYCLE_ID.name)).build();
				return inventory;
			}
		} finally {
			close(statement);
		}
		return null;
	}

	public int countAllInventorys() throws SQLException, Exception {
		Statement statement = null;
		int count = 0;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
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

	public enum Column {
		DESCRIPTION("description", 55), //
		PART_NUMBER("partNumber", 25), //
		PRICE("price", 40), //
		QUANTITY("quantity", 25), //
		MOTORCYCLE_ID("motorcycleId", 55);

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}
	}
}
