/**
 * Project: A00979176Assignment2
 * File: Validator.java
 * Date: July 1, 2017
 */

package a00979176.data.util;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class Validator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String YYYYMMDD_PATTERN = "(20\\d{2})(\\d{2})(\\d{2})";

	private Validator() {
	}

	public static boolean validateEmail(final String email) {
		return email.matches(EMAIL_PATTERN);
	}

	public static boolean validateJoinedDate(String yyyymmdd) {
		return yyyymmdd.matches(YYYYMMDD_PATTERN);
	}

}
