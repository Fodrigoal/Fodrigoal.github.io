/**
 * Project: A00979176Assignment2
 * File: MotorcycleReader.java
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
import a00979176.data.Motorcycle;
import a00979176.database.dao.MotorcycleDao;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class MotorcycleReader {

	public static final String RECORD_DELIMITER = ":";
	public static final String FIELD_DELIMITER = "\\|";

	public static void read(File motorcycleDataFile, MotorcycleDao dao) throws ApplicationException {
		BufferedReader motorcycleReader = null;
		try {
			motorcycleReader = new BufferedReader(new FileReader(motorcycleDataFile));

			String line = null;
			line = motorcycleReader.readLine(); // skip the header line
			while ((line = motorcycleReader.readLine()) != null) {
				Motorcycle motorcycle = readMotorcycleString(line);
				try {
					dao.add(motorcycle);
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			try {
				if (motorcycleReader != null) {
					motorcycleReader.close();
				}
			} catch (IOException e) {
				throw new ApplicationException(e.getMessage());
			}
		}
	}

	private static Motorcycle readMotorcycleString(String data) throws ApplicationException {
		String[] elements = data.split(FIELD_DELIMITER);
		if (elements.length != Motorcycle.ATTRIBUTE_COUNT) {
			throw new ApplicationException(String.format("Expected %d but got %d: %s", Motorcycle.ATTRIBUTE_COUNT,
					elements.length, Arrays.toString(elements)));
		}
		int index = 0;
		long id = Integer.parseInt(elements[index++]);
		String make = elements[index++];
		String model = elements[index++];
		int year = Integer.parseInt(elements[index++]);
		String serialNumber = elements[index++];
		int mileage = Integer.parseInt(elements[index++]);
		long customerId = Long.parseLong(elements[index++]);

		return new Motorcycle.Builder(id).setMake(make).setModel(model).setYear(year).setSerialNumber(serialNumber)
				.setMileage(mileage).setCustomerId(customerId).build();
	}
}
