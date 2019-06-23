/**
 * Project: A00979176Assignment2
 * File: InventoryReader.java
 * Date: July 1, 2017
 */

package a00979176.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import a00979176.ApplicationException;
import a00979176.data.Inventory;
import a00979176.database.dao.InventoryDao;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class InventoryReader {

	public static final String RECORD_DELIMITER = ":";
	public static final String FIELD_DELIMITER = "\\|";

	public static void read(File inventoryDataFile, InventoryDao dao) throws ApplicationException {
		BufferedReader inventoryReader = null;
		try {
			inventoryReader = new BufferedReader(new FileReader(inventoryDataFile));
			String line = null;
			line = inventoryReader.readLine(); // skip the header line
			while ((line = inventoryReader.readLine()) != null) {
				Inventory inventory = readInventoryString(line);
				try {
					dao.add(inventory);
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			try {
				if (inventoryReader != null) {
					inventoryReader.close();
				}
			} catch (IOException e) {
				throw new ApplicationException(e.getMessage());
			}
		}
	}

	private static Inventory readInventoryString(String data) throws ApplicationException {
		String[] elements = data.split(FIELD_DELIMITER);
		if (elements.length != Inventory.ATTRIBUTE_COUNT) {
			throw new ApplicationException(String.format("Expected %d but got %d: %s", Inventory.ATTRIBUTE_COUNT,
					elements.length, Arrays.toString(elements)));
		}
		int index = 0;
		String motocycleId = elements[index++];
		String description = elements[index++];
		String partNumber = elements[index++];
		int price = Integer.parseInt(elements[index++]);
		int quantity = Integer.parseInt(elements[index++]);

		return new Inventory.Builder(partNumber).setDescription(description).setPrice(price).setQuantity(quantity)
				.setMotorcycleId(motocycleId).build();
	}
}
