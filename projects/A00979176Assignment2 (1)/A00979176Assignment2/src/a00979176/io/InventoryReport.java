/**
 * Project: A00979176Assignment2
 * File: InventoryReport.java
 * Date: July 1, 2017
 */

package a00979176.io;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import a00979176.BcmcOptions;
import a00979176.data.AllData;
import a00979176.data.Inventory;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class InventoryReport {

	public static final String REPORT_FILE = "inventory_report.txt";
	public static final String HORIZONTAL_LINE = "-------------------------------------------------------------------------------------------";
	public static final String HORIZONTAL_LINE_2 = "-------------------------------------------------------------------------------------------------------";
	public static final String HEADER_FORMAT = "%-28s %-28s %-12s %8s %11s";
	public static final String HEADER_FORMAT_2 = "%-28s %-28s %-12s %8s %11s %11s";
	public static final String ROW_FORMAT = "%-28s %-28s %-12s %,8.2f %,11d";
	public static final String ROW_FORMAT_2 = "%-28s %-28s %-12s %,8.2f %11d %,11.2f";

	public static void print(PrintStream out) {
		String text = null;
		boolean hasTotal = BcmcOptions.isTotalOptionSet();

		println("Inventory Report", out);

		if (hasTotal) {
			println(HORIZONTAL_LINE_2, out);
			text = String.format(HEADER_FORMAT_2, "Make+Model", "Description", "Part#", "Price", "Quantity", "Value");
			println(text, out);
			println(HORIZONTAL_LINE_2, out);
		} else {
			println(HORIZONTAL_LINE, out);
			text = String.format(HEADER_FORMAT, "Make+Model", "Description", "Part#", "Price", "Quantity");
			println(text, out);
			println(HORIZONTAL_LINE, out);
		}
		Collection<Inventory> inventory = AllData.getInventory().values();

		if (BcmcOptions.isByDescriptionOptionSet()) {
			List<Inventory> list = new ArrayList<>(inventory);
			if (BcmcOptions.isDescendingOptionSet()) {
				Collections.sort(list, new CompareByDescriptionDescending());
			} else {
				Collections.sort(list, new CompareByDescription());
			}
			inventory = list;
		}

		if (BcmcOptions.isByCountOptionSet()) {
			List<Inventory> list = new ArrayList<>(inventory);
			if (BcmcOptions.isDescendingOptionSet()) {
				Collections.sort(list, new CompareByCountDescending());
			} else {
				Collections.sort(list, new CompareByCount());
			}
			inventory = list;
		}

		String make = BcmcOptions.getMake();
		if (make != null) {
			make = make.toLowerCase();
		}
		long total = 0;
		for (Inventory item : inventory) {
			String motorcycleId = item.getMotorcycleId();
			if (make != null && !motorcycleId.toLowerCase().contains(make)) {
				continue;
			}
			int price = item.getPrice();
			int quantity = item.getQuantity();

			if (hasTotal) {
				long value = (price * quantity);
				total += value;
				text = String.format(ROW_FORMAT_2, motorcycleId, item.getDescription(), item.getPartNumber(),
						price / 100.0f, quantity, value / 100.0f);
			} else {
				text = String.format(ROW_FORMAT, motorcycleId, item.getDescription(), item.getPartNumber(),
						price / 100.0f, quantity);
			}
			println(text, out);
		}
		if (hasTotal) {
			text = String.format("Value of current inventory: $%,.2f", total / 100.0f);
			println(text, out);
		}
	}

	public static String generateTotalInventory() {
		StringBuilder output = new StringBuilder();
		Collection<Inventory> inventory = AllData.getInventory().values();
		double total = 0;
		double value;
		for (Inventory item : inventory) {
			double price = item.getPrice() / 100.0;
			int quantity = item.getQuantity();
			value = 0;
			value = price * quantity;
			total += value;
		}
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		output.append("The total value in the inventory is ");
		output.append(currencyFormat.format(total));
		return output.toString();
	}

	public static String generateInventoryUIReport(String make, boolean descending, String typeReport) {
		StringBuilder text = new StringBuilder();
		String newLine = System.lineSeparator();
		text.append(HORIZONTAL_LINE);
		text.append(newLine);
		text.append(String.format(HEADER_FORMAT, "Make+Model", "Description", "Part#", "Price", "Quantity"));
		text.append(newLine);
		text.append(HORIZONTAL_LINE);
		text.append(newLine);
		Collection<Inventory> inventory = AllData.getInventory().values();

		if ("description".equalsIgnoreCase(typeReport)) {
			List<Inventory> list = new ArrayList<>(inventory);
			if (descending) {
				Collections.sort(list, new CompareByDescriptionDescending());
			} else {
				Collections.sort(list, new CompareByDescription());
			}
			inventory = list;
		}

		if ("count".equalsIgnoreCase(typeReport)) {
			List<Inventory> list = new ArrayList<>(inventory);
			if (descending) {
				Collections.sort(list, new CompareByCountDescending());
			} else {
				Collections.sort(list, new CompareByCount());
			}
			inventory = list;
		}

		if (make != null) {
			make = make.toLowerCase();
		}
		int total = 0;
		for (Inventory item : inventory) {
			String motorcycleId = item.getMotorcycleId();
			if (make != null && !motorcycleId.toLowerCase().contains(make)) {
				continue;
			}
			int price = item.getPrice();
			int quantity = item.getQuantity();

			text.append(String.format(ROW_FORMAT, motorcycleId, item.getDescription(), item.getPartNumber(),
					price / 100.0f, quantity));
			text.append(newLine);
			total++;
		}
		if (total == 0) {
			text = new StringBuilder();
		}
		return text.toString();
	}

	private static void println(String text, PrintStream out) {
		out.println(text);
	}

	public static class CompareByDescription implements Comparator<Inventory> {
		@Override
		public int compare(Inventory inventory1, Inventory inventory2) {
			return inventory1.getDescription().compareTo(inventory2.getDescription());
		}
	}

	public static class CompareByDescriptionDescending implements Comparator<Inventory> {
		@Override
		public int compare(Inventory inventory1, Inventory inventory2) {
			return inventory2.getDescription().compareTo(inventory1.getDescription());
		}
	}

	public static class CompareByCount implements Comparator<Inventory> {
		@Override
		public int compare(Inventory inventory1, Inventory inventory2) {
			return inventory1.getQuantity() - inventory2.getQuantity();
		}
	}

	public static class CompareByCountDescending implements Comparator<Inventory> {
		@Override
		public int compare(Inventory inventory1, Inventory inventory2) {
			return inventory2.getQuantity() - inventory1.getQuantity();
		}
	}
}
