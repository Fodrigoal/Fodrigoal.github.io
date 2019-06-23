/**
 * Project: A00979176Assignment2
 * File: MotorcycleReportDialog.java
 * Date: July 2, 2017
 */

package a00979176.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import a00979176.ApplicationException;
import a00979176.data.AllData;
import a00979176.data.Customer;
import a00979176.data.util.Validator;
import a00979176.database.Database;
import a00979176.ui.Verifier;
import net.miginfocom.swing.MigLayout;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
@SuppressWarnings("serial")
public class CustomerAddDialog extends JDialog {

	private Database db;
	private static Logger LOG = LogManager.getLogger();
	public static final String DB_PROPERTIES_FILENAME = "db.properties";
	private final JPanel contentPanel = new JPanel();
	private Properties properties;
	private JTextField idField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField streetField;
	private JTextField cityField;
	private JTextField postalCodeField;
	private JTextField phoneField;
	private JTextField emailField;
	private JTextField joinedDateField;

	public CustomerAddDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(450, 375);
		setLocationRelativeTo(null);
		setTitle("Customer Details");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][]"));

		// Creating the Customer ID field and it's value
		JLabel lblId = new JLabel("Customer ID");
		contentPanel.add(lblId, "cell 0 0,alignx trailing");
		idField = new JTextField();
		idField.setEnabled(false);
		idField.setEditable(false);
		contentPanel.add(idField, "cell 1 0,growx");
		idField.setColumns(10);
		idField.setText(Long.toString(getMaxId() + 1));

		// Creating the First Name field and it's validator
		JLabel lblFirstName = new JLabel("First Name");
		contentPanel.add(lblFirstName, "cell 0 1,alignx trailing");
		firstNameField = new JTextField();
		contentPanel.add(firstNameField, "cell 1 1,growx");
		firstNameField.setColumns(10);
		firstNameField.setInputVerifier(new Verifier("firstName"));

		// Creating the Last Name field and it's validator
		JLabel lblLastName = new JLabel("Last Name");
		contentPanel.add(lblLastName, "cell 0 2,alignx trailing");
		lastNameField = new JTextField();
		contentPanel.add(lastNameField, "cell 1 2,growx");
		lastNameField.setColumns(10);
		lastNameField.setInputVerifier(new Verifier("lastName"));

		// Creating the Street Name field and it's validator
		JLabel lblStreet = new JLabel("Street");
		contentPanel.add(lblStreet, "cell 0 3,alignx trailing");
		streetField = new JTextField();
		contentPanel.add(streetField, "cell 1 3,growx");
		streetField.setColumns(10);
		streetField.setInputVerifier(new Verifier("street"));

		// Creating the City field and it's validator
		JLabel lblCity = new JLabel("City");
		contentPanel.add(lblCity, "cell 0 4,alignx trailing");
		cityField = new JTextField();
		contentPanel.add(cityField, "cell 1 4,growx");
		cityField.setColumns(10);
		cityField.setInputVerifier(new Verifier("city"));

		// Creating the Postal Code field and it's validator
		JLabel lblPostalCode = new JLabel("Postal Code");
		contentPanel.add(lblPostalCode, "cell 0 5,alignx trailing");
		postalCodeField = new JTextField();
		contentPanel.add(postalCodeField, "cell 1 5,growx");
		postalCodeField.setColumns(10);
		postalCodeField.setInputVerifier(new Verifier("postalCode"));

		// Creating the Phone field and it's validator
		JLabel lblPhone = new JLabel("Phone");
		contentPanel.add(lblPhone, "cell 0 6,alignx trailing");
		phoneField = new JTextField();
		contentPanel.add(phoneField, "cell 1 6,growx");
		phoneField.setColumns(10);
		phoneField.setInputVerifier(new Verifier("phone"));

		// Creating the Email field and it's validator
		JLabel lblEmail = new JLabel("Email Address");
		contentPanel.add(lblEmail, "cell 0 7,alignx trailing");
		emailField = new JTextField();
		contentPanel.add(emailField, "cell 1 7,growx");
		emailField.setColumns(10);
		emailField.setInputVerifier(new Verifier("email"));

		// Creating the Joined Date field and it's value
		JLabel lblJoinedDate = new JLabel("Joined Date");
		contentPanel.add(lblJoinedDate, "cell 0 8,alignx trailing");
		joinedDateField = new JTextField();
		joinedDateField.setEnabled(false);
		joinedDateField.setEditable(false);
		contentPanel.add(joinedDateField, "cell 1 8,growx");
		joinedDateField.setColumns(10);
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String formattedString = localDate.format(formatter);
		joinedDateField.setText(formattedString);

		// Create new Panel for the buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		// Create the "Add Customer" button and it's listener
		JButton addButton = new JButton("Add Customer");
		addButton.setBackground(Color.GREEN);
		addButton.setForeground(Color.BLACK);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					AddCustomer();
				} catch (IOException | ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});
		addButton.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (addButton.getModel().isRollover()) {
					addButton.setBackground(new Color(30, 249, 30));
				} else {
					addButton.setBackground(Color.GREEN);
				}
			}
		});
		addButton.setActionCommand("Add Customer");
		buttonPane.add(addButton);

		// Create the "Cancel" button and it's listener
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setVerifyInputWhenFocusTarget(false);
		cancelButton.setBackground(Color.YELLOW);
		cancelButton.setForeground(Color.BLACK);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CustomerAddDialog.this.getParent().setVisible(false);
				CustomerAddDialog.this.dispose();
				CustomerDialog dialog = new CustomerDialog();
				dialog.setVisible(true);
			}
		});
		cancelButton.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (cancelButton.getModel().isRollover()) {
					cancelButton.setBackground(new Color(255, 255, 100));
				} else {
					cancelButton.setBackground(Color.YELLOW);
				}
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		// Closes the window when the 'Esc' key is pressed.
		KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		Action escapeAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				CustomerAddDialog.this.dispose();
			}
		};
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
		getRootPane().getActionMap().put("ESCAPE", escapeAction);

		// Setting the default button
		getRootPane().setDefaultButton(addButton);
	}

	/**
	 * Validate the new customer and adds it to the database if approved.
	 */
	protected void AddCustomer() throws FileNotFoundException, IOException, ApplicationException {
		long id = Long.valueOf(idField.getText());
		String firstName = firstNameField.getText();
		String lastName = lastNameField.getText();
		String street = streetField.getText();
		String city = cityField.getText();
		String postalCode = postalCodeField.getText();
		String phone = phoneField.getText();
		String email = emailField.getText();
		String joinedDate = joinedDateField.getText();

		/*
		 * Validation checks in case the user try to escape the setInputVerifier() by
		 * pressing enter to add the customer (this occurs because there`s no change of
		 * focus in this case).
		 */
		boolean allInputsValid = true;
		
		if (!Validator.validateFirstNameLastNameCityMakeOrModel(firstName)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, Verifier.getFirstNameLastNameCityMakeOrModel("first name"),
					"Sorry, but the first name seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateFirstNameLastNameCityMakeOrModel(lastName)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, Verifier.getFirstNameLastNameCityMakeOrModel("last name"),
					"Sorry, but the last name seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateStreet(street)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure that the street field starts with a number.",
					"Sorry, but the street seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateFirstNameLastNameCityMakeOrModel(city)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, Verifier.getFirstNameLastNameCityMakeOrModel("city"),
					"Sorry, but the city seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validatePostalCode(postalCode)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, Verifier.getPostalCodePanel(),
					"Sorry, but the postal code seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validatePhone(phone)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure your phone is 10 numbers or follow the format (604) 723-4937.",
					"Sorry, but the phone seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateEmail(email)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure that this is a valid email format with @ and a domain.",
					"Sorry, but the email seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} 

		if (allInputsValid == true) {
			int year = Integer.parseInt(joinedDate.substring(0, 4));
			int month = Integer.parseInt(joinedDate.substring(4, 6));
			int day = Integer.parseInt(joinedDate.substring(6, 8));

			// Creates the new customer
			Customer customer = new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName)
					.setStreet(street).setCity(city).setPhone(phone).setPostalCode(postalCode).setEmailAddress(email)
					.setJoinedDate(year, month, day).build();

			try {
				// Adding the created customer to the database
				AllData.getCustomerDao().add(customer);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}

			// Reloading new Customer Dialog
			properties = new Properties();
			properties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
			db = new Database(properties);
			AllData.loadData(db);
			this.getParent().setVisible(false);
			CustomerDialog dialog = new CustomerDialog();
			dialog.setVisible(true);
		}
	}

	/**
	 * @return the max ID number
	 */
	public long getMaxId() {
		List<Customer> customers = new ArrayList<Customer>();
		customers.addAll(AllData.getCustomers().values());
		long maxId = 0;

		for (Customer cus : customers) {
			long id = cus.getId();
			if (id > maxId) {
				maxId = id;
			}
		}
		return maxId;
	}
}
