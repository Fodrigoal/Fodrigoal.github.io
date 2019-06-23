/**
 * Project: A00979176Assignment2
 * File: Validator.java
 * Date: July 1, 2017
 */

package a00979176.data.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import a00979176.data.AllData;
import a00979176.data.Customer;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class Validator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String YYYYMMDD_PATTERN = "(20\\d{2})(\\d{2})(\\d{2})";
	private static final String POSTALCODE_PATTERN = "^(?!.*[DFIOQUdfioqu])[A-VXYa-vxy][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]$";
	private static final String SERIALNUMBER_PATTERN = "[a-zA-Z]{1}\\d{5}";

	/**
	 * Validate user first, last name or city, or the Motorcycle Make or Model for
	 * the following patterns: - The name or city is not empty. - The name or city
	 * does not start with a non-name character. - The name or city is between 1 and
	 * 25 characters. - The name or city can only start with an a-z (any case)
	 * character. - After that the name or city can contain a-z (any case) and ['-].
	 * - The name or city can only end with an a-z (ignore case) character.
	 */
	public static boolean validateFirstNameLastNameCityMakeOrModel(String input) {
		if (input.trim().length() == 0 || input.length() >= 20 || !input.matches("[a-zA-Z+='-]+")
				|| !Character.isLetter(input.charAt(0)) || !Character.isLetter(input.charAt(input.length() - 1))) {
			return false;
		}
		return true;
	}

	public static boolean validateStreet(String street) {
		if (street.trim().length() == 0) {
			return false;
		}
		return Character.isDigit(street.charAt(0));
	}

	public static boolean validatePostalCode(String postalCode) {
		Pattern pattern = Pattern.compile(POSTALCODE_PATTERN);
		if (postalCode.length() == 7) {
			System.out.println(postalCode);
			Matcher matcher = pattern.matcher(postalCode);

			if (matcher.matches()) {
				return true;
			}
		}
		if (postalCode.length() == 6) {
			String[] parts = { postalCode.substring(0, 3), postalCode.substring(3) };
			String newPostalCode = parts[0] + " " + parts[1];
			Matcher matcher = pattern.matcher(newPostalCode);

			if (matcher.matches()) {
				return true;
			}
		}
		return false;
	}

	public static boolean validatePhone(String phone) {
		if (phone.length() == 10 && phone.matches("\\d+")) {
			return true;
		}
		if (phone.length() == 12) {
			String[] parts = { phone.substring(0, 3), phone.substring(4, 7), phone.substring(8) };
			String newPhone = parts[0] + parts[1] + parts[2];
			if (newPhone.length() == 10 && newPhone.matches("\\d+")) {
				return true;
			}
		}
		if (phone.length() == 13) {
			String[] parts = { phone.substring(1, 4), phone.substring(5, 8), phone.substring(9) };
			String newPhone = parts[0] + parts[1] + parts[2];
			String[] parts2 = { phone.substring(1, 4), phone.substring(6, 9), phone.substring(9) };
			String newPhone2 = parts2[0] + parts2[1] + parts2[2];
			if (newPhone.length() == 10 && newPhone.matches("\\d+")
					|| newPhone2.length() == 10 && newPhone2.matches("\\d+")) {
				return true;
			}
		}
		if (phone.length() == 14) {
			String[] parts = { phone.substring(1, 4), phone.substring(6, 9), phone.substring(10) };
			String newPhone = parts[0] + parts[1] + parts[2];
			if (newPhone.length() == 10 && newPhone.matches("\\d+")) {
				return true;
			}
		}

		return false;
	}

	public static boolean validateEmail(final String email) {
		return email.matches(EMAIL_PATTERN);
	}

	public static boolean validateJoinedDate(String yyyymmdd) {
		return yyyymmdd.matches(YYYYMMDD_PATTERN);
	}

	public static boolean validateMotorcycleId(String motorcycleId) {
		if (motorcycleId.contains("+")) {
			String parts[] = motorcycleId.split("\\+");

			if (parts.length == 2) {
				return true;
			}
		}
		return false;
	}

	public static boolean validateDescription(String description) {
		if (description.length() <= 55) {
			return true;
		}
		return false;
	}

	public static boolean validatePrice(String price) {
		if (price.contains(".")) {
			String newPrice = price.split("\\.", 2)[0];
			
			if (newPrice.matches("\\d+")) {
				return true;
			}
		}
		if (price.matches("\\d+")) {
			return true;
		}
		
		return false;
	}

	public static boolean validateQuantity(String number) {
		if (number.matches("\\d+") && number.length() <= 9) {
			return true;
		}
		return false;
	}
	
	public static boolean validateYear(String year) {
		if (year.length() == 4 && year.matches("\\d+")) {
			return true;
		}
		return false;
	}

	public static boolean validateSerialNumber(String serialNumber) {
		return serialNumber.matches(SERIALNUMBER_PATTERN);
	}

	public static boolean validateMileage(String number) {
		if (number.matches("\\d+")) {
			return true;
		}
		return false;
	}

	public static boolean validateCustomerId(String customerId) {
		if (customerId.matches("\\d+")) {
			long id = Long.parseLong(customerId);

			List<Customer> customer = new ArrayList<Customer>();
			customer.addAll(AllData.getCustomers().values());

			for (Customer cus : customer) {
				if (id == cus.getId()) {
					return true;
				}
			}
		}
		return false;
	}

}
