/**
 * Project: A00979176Assignment2
 * File: CustomersReport.java
 * Date: July 1, 2017
 */

package a00979176.io;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import a00979176.BcmcOptions;
import a00979176.data.AllData;
import a00979176.data.Customer;
import a00979176.data.util.Common;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class CustomersReport {

	public static final String REPORT_FILE = "customers_report.txt";
	public static final String HORIZONTAL_LINE = "-------------------------------------------------------------------------------------------------------------------------------------------------------";
	public static final String HEADER_FORMAT = "%3s. %-6s %-12s %-12s %-25s %-12s %-12s %-15s %-25s %6s %9s";
	public static final String ROW_FORMAT = "%3d. %06d %-12s %-12s %-25s %-12s %-12s %-15s %-25s %6s %7s";

	public static void print(PrintStream out) {
		String text = null;
		println("Customers Report", out);
		println(HORIZONTAL_LINE, out);
		text = String.format(HEADER_FORMAT, "#", "ID", "First name", "Last name", "Street", "City", "Postal Code",
				"Phone", "Email", "Join Date", "Length");
		println(text, out);
		println(HORIZONTAL_LINE, out);
		Collection<Customer> customers = AllData.getCustomers().values();

		if (BcmcOptions.isByJoinDateOptionSet()) {
			List<Customer> list = new ArrayList<>(customers);
			if (BcmcOptions.isDescendingOptionSet()) {
				Collections.sort(list, new CompareByJoinedDateDescending());
			} else {
				Collections.sort(list, new CompareByJoinedDate());
			}
			customers = list;
		}
		int i = 0;
		for (Customer customer : customers) {
			LocalDate date = customer.getJoinedDate();
			text = String.format(ROW_FORMAT, ++i, customer.getId(), customer.getFirstName(), customer.getLastName(),
					customer.getStreet(), customer.getCity(), customer.getPostalCode(), customer.getPhone(),
					customer.getEmailAddress(), Common.DATE_FORMAT.format(date), calculateJoinDuration(date));
			println(text, out);
		}
	}

	private static void println(String text, PrintStream out) {
		out.println(text);
	}

	private static long calculateJoinDuration(LocalDate date) {
		return ChronoUnit.YEARS.between(date, LocalDate.now());
	}

	public static class CompareByJoinedDate implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return customer1.getJoinedDate().compareTo(customer2.getJoinedDate());
		}
	}

	public static class CompareByJoinedDateDescending implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return customer2.getJoinedDate().compareTo(customer1.getJoinedDate());
		}
	}
}
