package a00979176.ui;

import java.awt.GridLayout;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import a00979176.data.util.Validator;

public class Verifier extends InputVerifier {

	private String toValidate;

	public Verifier(String toValidate) {
		this.toValidate = toValidate;
	}

	public boolean verify(JComponent input) {
		JTextField tf = (JTextField) input;

		switch (toValidate) {
		case "firstName":
			if (!Validator.validateFirstNameLastNameCityMakeOrModel(tf.getText())) {
				JOptionPane.showMessageDialog(null, getFirstNameLastNameCityMakeOrModel("first name"),
						"Sorry, but the first name seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "lastName":
			if (!Validator.validateFirstNameLastNameCityMakeOrModel(tf.getText())) {
				JOptionPane.showMessageDialog(null, getFirstNameLastNameCityMakeOrModel("last name"),
						"Sorry, but the last name seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "street":
			if (!Validator.validateStreet(tf.getText())) {
				JOptionPane.showMessageDialog(null, "Please ensure that the street field starts with a number.",
						"Sorry, but the street seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "city":
			if (!Validator.validateFirstNameLastNameCityMakeOrModel(tf.getText())) {
				JOptionPane.showMessageDialog(null, getFirstNameLastNameCityMakeOrModel("city"),
						"Sorry, but the street seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "postalCode":
			if (!Validator.validatePostalCode(tf.getText())) {
				JOptionPane.showMessageDialog(null, getPostalCodePanel(),
						"Sorry, but the postal code seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "phone":
			if (!Validator.validatePhone(tf.getText())) {
				JOptionPane.showMessageDialog(null,
						"Please ensure your phone is 10 numbers or follow the format (604) 723-4937.",
						"Sorry, but the phone seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "email":
			if (!Validator.validateEmail(tf.getText())) {
				JOptionPane.showMessageDialog(null,
						"Please ensure that this is a valid email format with @ and a domain.",
						"Sorry, but the email seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "motorcycleId":
			if (!Validator.validateMotorcycleId(tf.getText())) {
				JOptionPane.showMessageDialog(null,
						"Please ensure that motorcycle+maker follows the format Make+Model.",
						"Sorry, but the motorcycle+maker seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "description":
			if (!Validator.validateDescription(tf.getText())) {
				JOptionPane.showMessageDialog(null, "Please ensure that description is less than 55 characters.",
						"Sorry, but the description number seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "price":
			if (!Validator.validatePrice(tf.getText())) {
				JOptionPane.showMessageDialog(null,
						"Please ensure that price is only numbers with the 2 cents digits in the end.",
						"Sorry, but the make seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "quantity":
			if (!Validator.validateQuantity(tf.getText())) {
				JOptionPane.showMessageDialog(null, "Please ensure that quantity only contains numbers and has less than 9 digits.",
						"Sorry, but the quantity seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "make":
			if (!Validator.validateFirstNameLastNameCityMakeOrModel(tf.getText())) {
				JOptionPane.showMessageDialog(null, getFirstNameLastNameCityMakeOrModel("make"),
						"Sorry, but the make seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "model":
			if (!Validator.validateFirstNameLastNameCityMakeOrModel(tf.getText())) {
				JOptionPane.showMessageDialog(null, getFirstNameLastNameCityMakeOrModel("model"),
						"Sorry, but the model seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "year":
			if (!Validator.validateYear(tf.getText())) {
				JOptionPane.showMessageDialog(null, "Please ensure your year is 4 numbers.",
						"Sorry, but '" + tf.getText() + "' seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "serialNumber":
			if (!Validator.validateSerialNumber(tf.getText())) {
				JOptionPane.showMessageDialog(null, "Please ensure your serial number follows the format A12345.",
						"Sorry, but the serial number seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "mileage":
			if (!Validator.validateMileage(tf.getText())) {
				JOptionPane.showMessageDialog(null, "Please ensure that mileage only contains number.",
						"Sorry, but the mileage seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		case "customerId":
			if (!Validator.validateCustomerId(tf.getText())) {
				JOptionPane.showMessageDialog(null, "Please ensure that your customer ID is an existing one.",
						"Sorry, but the customer ID seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
			} else {
				return true;
			}
			break;
		}
		return false;
	}

	public static JPanel getFirstNameLastNameCityMakeOrModel(String name) {
		JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
		JLabel message = getLabel("Please ensure that the " + name + " field:");
		JLabel requirement1 = getLabel("- Is not Empty.");
		JLabel requirement2 = getLabel("- Does not start with a non-name character.");
		JLabel requirement3 = getLabel("- Is between 1 and 20 characters.");
		JLabel requirement4 = getLabel("- Starts with an a-z (any case) character.");
		JLabel requirement5 = getLabel("- After that the " + name + " can also contain ' or -");
		JLabel requirement6 = getLabel("- Ends with an a-z (ignore case) character.");

		panel.add(message);
		panel.add(requirement1);
		panel.add(requirement2);
		panel.add(requirement3);
		panel.add(requirement4);
		panel.add(requirement5);
		panel.add(requirement6);

		return panel;
	}

	private static JLabel getLabel(String title) {
		return new JLabel(title);
	}

	public static JPanel getPostalCodePanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
		JLabel message = getLabel("Please ensure that the postal code field:");
		JLabel requirement1 = getLabel("- Is only 6 or 7 (if you use space) characters.");
		JLabel requirement2 = getLabel("- Follows the format A1A1A1 or A1A 1A1, any case.");
		JLabel requirement3 = getLabel("- Does not include the letters D, F, I, O, Q or U");
		JLabel requirement4 = getLabel("- The first letter is not W or Z.");

		panel.add(message);
		panel.add(requirement1);
		panel.add(requirement2);
		panel.add(requirement3);
		panel.add(requirement4);

		return panel;
	}
}
