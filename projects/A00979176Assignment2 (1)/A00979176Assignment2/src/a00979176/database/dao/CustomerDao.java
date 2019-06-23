/**
 * Project: A00979176Assignment2
 * File: CustomerDao.java
 * Date: July 1, 2017
 */

package a00979176.database.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00979176.ApplicationException;
import a00979176.data.Customer;
import a00979176.database.Database;
import a00979176.database.util.DbConstants;
import a00979176.io.CustomerReader;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class CustomerDao extends Dao {

	public static final String TABLE_NAME = DbConstants.CUSTOMER_TABLE_NAME;
	private static Logger LOG = LogManager.getLogger();
	private static final String CUSTOMERS_DATA_FILE = "customers.dat";

	public CustomerDao(Database database) throws ApplicationException {
		super(database, TABLE_NAME);
		LOG.debug("Constructor CustomerDao()");
		load(new File(CUSTOMERS_DATA_FILE));
	}

	public void load(File customerData) throws ApplicationException {
		LOG.debug("CustomerDao load()");
		try {
			if (!Database.tableExists(CustomerDao.TABLE_NAME) || Database.dbTableDropRequested()) {
				if (Database.tableExists(CustomerDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}
				create();
				LOG.debug("Inserting the customers");
				if (!customerData.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", CUSTOMERS_DATA_FILE));
				}
				CustomerReader.read(customerData, this);
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			throw new ApplicationException(e);
		}
	}

	@Override
	public void create() throws SQLException {
		LOG.debug("Creating database table " + TABLE_NAME);
		String sqlString = String.format("CREATE TABLE %s(" // 1
				+ "%s BIGINT, " // 2
				+ "%s VARCHAR(%d), " // 3
				+ "%s VARCHAR(%d), " // 4
				+ "%s VARCHAR(%d), " // 5
				+ "%s VARCHAR(%d), " // 6
				+ "%s VARCHAR(%d), " // 7
				+ "%s VARCHAR(%d), " // 8
				+ "%s VARCHAR(%d), " // 9
				+ "%s TIMESTAMP, " // 10
				+ "PRIMARY KEY (%s))", // 11
				TABLE_NAME, // 1
				Column.ID.name, // 2
				Column.FIRST_NAME.name, Column.FIRST_NAME.length, // 3
				Column.LAST_NAME.name, Column.LAST_NAME.length, // 4
				Column.STREET.name, Column.STREET.length, // 5
				Column.CITY.name, Column.CITY.length, // 6
				Column.POSTAL_CODE.name, Column.POSTAL_CODE.length, // 7
				Column.PHONE.name, Column.PHONE.length, // 8
				Column.EMAIL_ADDRESS.name, Column.EMAIL_ADDRESS.length, // 9
				Column.JOINED_DATE.name, // 10
				Column.ID.name);// 11
		super.create(sqlString);
	}

	public void add(Customer customer) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		execute(sqlString, customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getStreet(),
				customer.getCity(), customer.getPostalCode(), customer.getPhone(), customer.getEmailAddress(),
				toTimestamp(customer.getJoinedDate()));
	}

	public void update(Customer customer) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
				TABLE_NAME, Column.FIRST_NAME.name, Column.LAST_NAME.name, Column.STREET.name, Column.CITY.name,
				Column.POSTAL_CODE.name, Column.PHONE.name, Column.EMAIL_ADDRESS.name, Column.JOINED_DATE.name,
				Column.ID.name);
		LOG.debug("Update statment: " + sqlString);
		execute(sqlString, customer.getFirstName(), customer.getLastName(), customer.getStreet(),
				customer.getCity(), customer.getPostalCode(), customer.getPhone(), customer.getEmailAddress(),
				toTimestamp(customer.getJoinedDate()), customer.getId());
	}

	public void delete(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();
			String sqlString = String.format("DELETE FROM %s WHERE %s=%s", TABLE_NAME, Column.ID.name,
					customer.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public List<Long> getCustomerIds() throws SQLException {
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
		LOG.debug(String.format("Loaded %d customer(s) IDs from the database", ids.size()));

		return ids;
	}

	public List<Customer> getCustomers() throws SQLException {
		List<Customer> customers = new ArrayList<>();
		String selectString = String.format("SELECT * FROM %s", TABLE_NAME);
		LOG.debug(selectString);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				Timestamp timestamp = resultSet.getTimestamp(Column.JOINED_DATE.name);
				LocalDate date = timestamp.toLocalDateTime().toLocalDate();
				Customer customer = new Customer.Builder(resultSet.getInt(Column.ID.name),
						resultSet.getString(Column.PHONE.name))
								.setFirstName(resultSet.getString(Column.FIRST_NAME.name))
								.setLastName(resultSet.getString(Column.LAST_NAME.name))
								.setStreet(resultSet.getString(Column.STREET.name))
								.setCity(resultSet.getString(Column.CITY.name))
								.setPostalCode(resultSet.getString(Column.POSTAL_CODE.name))
								.setEmailAddress(resultSet.getString(Column.EMAIL_ADDRESS.name)).setJoinedDate(date)
								.build();
				System.out.println(customer);

				customers.add(customer);
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("Loaded %d customer(s) from the database", customers.size()));

		return customers;
	}

	public Customer getCustomer(Long customerId) throws SQLException, Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, customerId);
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
				Timestamp timestamp = resultSet.getTimestamp(Column.JOINED_DATE.name);
				LocalDate date = timestamp.toLocalDateTime().toLocalDate();
				Customer customer = new Customer.Builder(resultSet.getInt(Column.ID.name),
						resultSet.getString(Column.PHONE.name)) //
								.setFirstName(resultSet.getString(Column.FIRST_NAME.name)) //
								.setLastName(resultSet.getString(Column.LAST_NAME.name)) //
								.setStreet(resultSet.getString(Column.STREET.name)) //
								.setCity(resultSet.getString(Column.CITY.name)) //
								.setPostalCode(resultSet.getString(Column.POSTAL_CODE.name)) //
								.setEmailAddress(resultSet.getString(Column.EMAIL_ADDRESS.name)) //
								.setJoinedDate(date).build();

				return customer;
			}
		} finally {
			close(statement);
		}
		return null;
	}

	public int countAllCustomers() throws SQLException, Exception {
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
		ID("id", 16), //
		FIRST_NAME("firstName", 35), //
		LAST_NAME("lastName", 35), //
		STREET("street", 55), //
		CITY("city", 35), //
		POSTAL_CODE("postalCode", 10), //
		PHONE("phone", 15), //
		EMAIL_ADDRESS("emailAddress", 55), //
		JOINED_DATE("joinedDate", 10); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}
	}
}
