/**
 * Project: A00979176Assignment2
 * File: DbConstants.java
 * Date: July 1, 2017
 */
package a00979176.database.util;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public interface DbConstants {

	String DB_PROPERTIES_FILENAME = "db.properties";
	String DB_DRIVER_KEY = "db.driver";
	String DB_URL_KEY = "db.url";
	String DB_USER_KEY = "db.user";
	String DB_PASSWORD_KEY = "db.password";
	String TABLE_ROOT = "bcmc_";
	String INVENTORY_TABLE_NAME = TABLE_ROOT + "Inventory";
	String SERVICE_TABLE_NAME = TABLE_ROOT + "Service";
	String CUSTOMER_TABLE_NAME = TABLE_ROOT + "Customer";
	String MOTORCYCLE_TABLE_NAME = TABLE_ROOT + "Motorcycle";

}
