/**
 * Project: A00979176Assignment2
 * File: CustomerReader.java
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
import a00979176.data.Customer;
import a00979176.data.util.Validator;
import a00979176.database.dao.CustomerDao;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class CustomerReader {

	public static final String RECORD_DELIMITER = ":";
	public static final String FIELD_DELIMITER = "\\|";

	public static void read(File customerFile, CustomerDao dao) throws ApplicationException {
		BufferedReader customerReader = null;
		try {
			customerReader = new BufferedReader(new FileReader(customerFile));
			String line = null;
			line = customerReader.readLine(); // skip the header line
			while ((line = customerReader.readLine()) != null) {
				Customer customer = readCustomerString(line);
				try {
					dao.add(customer);
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			try {
				if (customerReader != null) {
					customerReader.close();
				}
			} catch (IOException e) {
				throw new ApplicationException(e.getMessage());
			}
		}
	}

	private static Customer readCustomerString(String data) throws ApplicationException {
		String[] customers = data.split(FIELD_DELIMITER);
		if (customers.length != Customer.ATTRIBUTE_COUNT) {
			throw new ApplicationException(String.format("Expected %d but got %d: %s", Customer.ATTRIBUTE_COUNT,
					customers.length, Arrays.toString(customers)));
		}

		int index = 0;
		long id = Integer.parseInt(customers[index++]);
		String firstName = customers[index++];
		String lastName = customers[index++];
		String street = customers[index++];
		String city = customers[index++];
		String postalCode = customers[index++];
		String phone = customers[index++];
		String emailAddress = customers[index++];
		if (!Validator.validateEmail(emailAddress)) {
			throw new ApplicationException(String.format("Invalid email: %s", emailAddress));
		}
		String yyyymmdd = customers[index];
		if (!Validator.validateJoinedDate(yyyymmdd)) {
			throw new ApplicationException(String.format("Invalid joined date: %s for customer %d", yyyymmdd, id));
		}
		int year = Integer.parseInt(yyyymmdd.substring(0, 4));
		int month = Integer.parseInt(yyyymmdd.substring(4, 6)) - 1;
		int day = Integer.parseInt(yyyymmdd.substring(6, 8));

		return new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName).setStreet(street)
				.setCity(city).setPostalCode(postalCode).setEmailAddress(emailAddress).setJoinedDate(year, month, day)
				.build();
	}
}
