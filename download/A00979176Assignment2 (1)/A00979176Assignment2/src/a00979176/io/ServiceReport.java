/**
 * Project: A00979176Assignment2
 * File: CustomersReport.java
 * Date: July 1, 2017
 */

package a00979176.io;

import java.io.PrintStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00979176.BcmcOptions;
import a00979176.data.AllData;
import a00979176.data.Customer;
import a00979176.data.Motorcycle;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class ServiceReport {

	public static final String REPORT_FILENAME = "service_report.txt";
	public static final String HORIZONTAL_LINE = "----------------------------------------------------------------------------------";
	public static final String HEADER_FORMAT = "%-12s %-12s %-20s %-20s %-6s %5s";
	public static final String SERVICE_FORMAT = "%-12s %-12s %-20s %-20s %-6s %,7d";

	private static final Logger LOG = LogManager.getLogger();

	public static void print(PrintStream out) {
		LOG.info("Generating service report.");
		String text = null;
		println("Services Report", out);
		println(HORIZONTAL_LINE, out);
		text = String.format(HEADER_FORMAT, "First name", "Last name", "Make", "Model", "Year", "Mileage");
		println(text, out);
		println(HORIZONTAL_LINE, out);

		String makeOption = BcmcOptions.getMake();
		Map<Long, Customer> customers = AllData.getCustomers();
		for (Motorcycle motorcycle : AllData.getMotorcycles().values()) {
			String make = motorcycle.getMake();
			if (makeOption != null && !make.equalsIgnoreCase(makeOption)) {
				continue;
			}
			long customerId = motorcycle.getCustomerId();
			Customer customer = customers.get(customerId);
			text = String.format(SERVICE_FORMAT, customer.getFirstName(), customer.getLastName(), motorcycle.getMake(),
					motorcycle.getModel(), motorcycle.getYear(), motorcycle.getMileage());
			println(text, out);
		}
	}

	private static void println(String text, PrintStream out) {
		out.println(text);
	}
}
