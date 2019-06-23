/**
 * Project: A00979176Assignment2
 * File: CustomerDao.java
 * Date: July 1, 2017
 */

package a00979176.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00979176.ApplicationException;
import a00979176.database.Database;
import a00979176.database.dao.CustomerDao;
import a00979176.database.dao.InventoryDao;
import a00979176.database.dao.MotorcycleDao;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class AllData {

	private static final Logger LOG = LogManager.getLogger();
	private static CustomerDao customerDao;
	private static MotorcycleDao motorcycleDao;
	private static InventoryDao inventoryDao;
	private static Map<Long, Motorcycle> motorcycles;
	private static Map<Long, Customer> customers;
	private static Map<String, Inventory> inventory;

	public static void loadData(Database db) throws ApplicationException {
		LOG.debug("loading data");

		motorcycles = new HashMap<Long, Motorcycle>();
		customers = new HashMap<Long, Customer>();
		inventory = new HashMap<String, Inventory>();
		customerDao = new CustomerDao(db);
		motorcycleDao = new MotorcycleDao(db);
		inventoryDao = new InventoryDao(db);

		try {
			for (Motorcycle motorcycle : motorcycleDao.getAllMotorcycles()) {
				motorcycles.put(motorcycle.getId(), motorcycle);
			}
			for (Customer customer : customerDao.getCustomers()) {
				customers.put(customer.getId(), customer);
			}
			for (Inventory inv : inventoryDao.getAllInventory()) {
				inventory.put(inv.getPartNumber(), inv);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		LOG.debug("Successfully loaded the data");
	}

	public static Map<Long, Customer> getCustomers() {
		return customers;
	}

	public static Map<Long, Motorcycle> getMotorcycles() {
		return motorcycles;
	}

	public static Map<String, Inventory> getInventory() {
		return inventory;
	}

	public static CustomerDao getCustomerDao() {
		return customerDao;
	}

	public static MotorcycleDao getMotorcycleDao() {
		return motorcycleDao;
	}

	public static InventoryDao getInventoryDao() {
		return inventoryDao;
	}
}
