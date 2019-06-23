/**
 * Project: A00979176Assignment2
 * File: BcmcOptions.java
 * Date: July 1, 2017
 */

package a00979176;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class BcmcOptions {

	private static CommandLine commandLine;
	private static boolean service;
	private static boolean inventory;
	private static boolean customers;

	private BcmcOptions() {
	}

	public static void process(String[] args) throws ParseException {
		commandLine = new DefaultParser().parse(createOptions(), args);

		if (args.length == 0) {
			service = true;
			inventory = true;
			customers = true;
		} else {
			service = commandLine.hasOption(Value.SERVICE.getOption());
			inventory = commandLine.hasOption(Value.INVENTORY.getOption());
			customers = commandLine.hasOption(Value.CUSTOMERS.getOption());
		}
	}

	public static boolean isServiceOptionSet() {
		return service;
	}

	public static boolean isInventoryOptionSet() {
		return inventory;
	}

	public static boolean isCustomersOptionSet() {
		return customers;
	}

	public static boolean isTotalOptionSet() {
		return commandLine.hasOption(Value.TOTAL.getOption());
	}

	public static boolean isByDescriptionOptionSet() {
		return commandLine.hasOption(Value.BY_DESCRIPTION.getOption());
	}

	public static boolean isByCountOptionSet() {
		return commandLine.hasOption(Value.BY_COUNT.getOption());
	}

	public static boolean isByJoinDateOptionSet() {
		return commandLine.hasOption(Value.BY_JOIN_DATE.getOption());
	}

	public static String getMake() {
		return commandLine.getOptionValue(Value.MAKE.getOption());
	}

	public static boolean isDescendingOptionSet() {
		return commandLine.hasOption(Value.DESCENDING.getOption());
	}

	public static boolean isHelpOptionSet() {
		return commandLine.hasOption(Value.HELP.getOption());
	}

	private static Options createOptions() {
		Options options = new Options();

		for (Value value : Value.values()) {
			Option option = null;

			if (value.hasArg) {
				option = Option.builder(value.option).longOpt(value.longOption).hasArg().desc(value.description)
						.build();
			} else {
				option = Option.builder(value.option).longOpt(value.longOption).desc(value.description).build();
			}
			options.addOption(option);
		}
		return options;
	}

	public enum Value {
		HELP("?", "help", false, "Display help"), //
		SERVICE("s", "service", false, "Run the Service report"), //
		INVENTORY("i", "inventory", false, "Run the Inventory report"), //
		CUSTOMERS("c", "customers", false, "Run the Customers report"), //
		TOTAL("t", "total", false, "Calculate the inventory item total and grand total for the selected inventory"), //
		BY_DESCRIPTION("D", "by_description", false, "Sort the inventory by description"), //
		BY_COUNT("C", "by_count", false, "Sort the inventory by quantity"), //
		BY_JOIN_DATE("J", "by_join_date", false, "Sort the customers by join date"), //
		MAKE("m", "make", true, "Filter by make - case insensitive"), //
		DESCENDING("d", "descending", false, "Any sort is now displayed in descending order");

		private final String option;
		private final String longOption;
		private final boolean hasArg;
		private final String description;

		Value(String option, String longOption, boolean hasArg, String description) {
			this.option = option;
			this.longOption = longOption;
			this.hasArg = hasArg;
			this.description = description;
		}

		public String getOption() {
			return option;
		}

		public String getLongOption() {
			return longOption;
		}

		public String getDescription() {
			return description;
		}

		public boolean hasArg() {
			return hasArg;
		}
	}
}
