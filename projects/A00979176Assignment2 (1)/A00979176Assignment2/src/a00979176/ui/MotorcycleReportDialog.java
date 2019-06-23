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
import a00979176.data.Motorcycle;
import a00979176.data.util.Validator;
import a00979176.database.Database;
import net.miginfocom.swing.MigLayout;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
@SuppressWarnings("serial")
public class MotorcycleReportDialog extends JDialog {

	private Database db;
	private Properties properties;
	public static final String DB_PROPERTIES_FILENAME = "db.properties";
	private static Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField idField;
	private JTextField makeField;
	private JTextField modelField;
	private JTextField yearField;
	private JTextField serialNumberField;
	private JTextField mileageField;
	private JTextField customerIdField;
	private Motorcycle motorcycle;

	public MotorcycleReportDialog(Motorcycle motorcycle) {
		this.motorcycle = motorcycle;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(450, 310);
		setLocationRelativeTo(null);
		setTitle("Motorcycle Details");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][]"));

		// Creating the Motorcycle ID field and making it immutable
		JLabel lblId = new JLabel("Motorcycle ID");
		contentPanel.add(lblId, "cell 0 0,alignx trailing");
		idField = new JTextField();
		idField.setEnabled(false);
		idField.setEditable(false);
		contentPanel.add(idField, "cell 1 0,growx");
		idField.setColumns(10);

		// Creating the Make field and it's validator
		JLabel lblMake = new JLabel("Make");
		contentPanel.add(lblMake, "cell 0 1,alignx trailing");
		makeField = new JTextField();
		contentPanel.add(makeField, "cell 1 1,growx");
		makeField.setColumns(10);
		makeField.setInputVerifier(new Verifier("make"));

		// Creating the Model field and it's validator
		JLabel lblModel = new JLabel("Model");
		contentPanel.add(lblModel, "cell 0 2,alignx trailing");
		modelField = new JTextField();
		contentPanel.add(modelField, "cell 1 2,growx");
		modelField.setColumns(10);
		modelField.setInputVerifier(new Verifier("model"));

		// Creating the Year field and it's validator
		JLabel lblYear = new JLabel("Year");
		contentPanel.add(lblYear, "cell 0 3,alignx trailing");
		yearField = new JTextField();
		contentPanel.add(yearField, "cell 1 3,growx");
		yearField.setColumns(10);
		yearField.setInputVerifier(new Verifier("year"));

		// Creating the Serial Number field and it's validator
		JLabel lblSerialNumber = new JLabel("Serial Number");
		contentPanel.add(lblSerialNumber, "cell 0 4,alignx trailing");
		serialNumberField = new JTextField();
		contentPanel.add(serialNumberField, "cell 1 4,growx");
		serialNumberField.setColumns(10);
		serialNumberField.setInputVerifier(new Verifier("serialNumber"));

		// Creating the Mileage field and it's validator
		JLabel lblMileage = new JLabel("Mileage");
		contentPanel.add(lblMileage, "cell 0 5,alignx trailing");
		mileageField = new JTextField();
		contentPanel.add(mileageField, "cell 1 5,growx");
		mileageField.setColumns(10);
		mileageField.setInputVerifier(new Verifier("mileage"));

		// Creating the Customer ID field and it's validator
		JLabel lblCustomerId = new JLabel("Customer ID");
		contentPanel.add(lblCustomerId, "cell 0 6,alignx trailing");
		customerIdField = new JTextField();
		contentPanel.add(customerIdField, "cell 1 6,growx");
		customerIdField.setColumns(10);
		customerIdField.setInputVerifier(new Verifier("customerId"));

		// Create new Panel for the buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		// Create the "Delete" button and it's listener
		JButton deleteButton = new JButton("Delete");
		deleteButton.setBackground(Color.RED);
		deleteButton.setForeground(Color.BLACK);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					deleteMotorcycle();
				} catch (IOException | ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});
		deleteButton.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (deleteButton.getModel().isRollover()) {
					deleteButton.setBackground(new Color(249, 32, 32));
				} else {
					deleteButton.setBackground(Color.RED);
				}
			}
		});
		deleteButton.setActionCommand("Delete");
		deleteButton.setVerifyInputWhenFocusTarget(false);
		buttonPane.add(deleteButton);

		// Create the "Update" button and it's listener
		JButton updateButton = new JButton("Update");
		updateButton.setBackground(Color.BLUE);
		updateButton.setForeground(Color.BLACK);
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					updateMotorcycle(motorcycle);
				} catch (IOException | ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});
		updateButton.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (updateButton.getModel().isRollover()) {
					updateButton.setBackground(new Color(47, 47, 249));
				} else {
					updateButton.setBackground(Color.BLUE);
				}
			}
		});
		updateButton.setActionCommand("Update");
		buttonPane.add(updateButton);

		// Create the "Cancel" button and it's listener
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBackground(Color.YELLOW);
		cancelButton.setForeground(Color.BLACK);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MotorcycleReportDialog.this.dispose();
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
		cancelButton.setVerifyInputWhenFocusTarget(false);
		buttonPane.add(cancelButton);

		// Closes the window when the 'Esc' key is pressed.
		KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		Action escapeAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				MotorcycleReportDialog.this.dispose();
			}
		};
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
		getRootPane().getActionMap().put("ESCAPE", escapeAction);

		// Setting the default button
		getRootPane().setDefaultButton(updateButton);

		// Setting the values at the fields
		setMotorcycle(motorcycle);
	}

	/**
	 * Sets the current motorcycle value at the input fields.
	 */
	public void setMotorcycle(Motorcycle motorcycle) {
		idField.setText(Long.toString(motorcycle.getId()));
		makeField.setText(motorcycle.getMake());
		modelField.setText(motorcycle.getModel());
		yearField.setText(Integer.toString(motorcycle.getYear()));
		serialNumberField.setText(motorcycle.getSerialNumber());
		mileageField.setText(Integer.toString(motorcycle.getMileage()));
		customerIdField.setText(Long.toString(motorcycle.getCustomerId()));
	}

	/**
	 * Validate the new values and updates the motorcycle at the the database if
	 * approved.
	 */
	protected void updateMotorcycle(Motorcycle motorcycle)
			throws FileNotFoundException, IOException, ApplicationException {
		long id = Long.valueOf(idField.getText());
		String make = makeField.getText();
		String model = modelField.getText();
		String year = yearField.getText();
		String serialNumber = serialNumberField.getText();
		String mileage = mileageField.getText();
		String customerId = customerIdField.getText();

		/*
		 * Validation checks in case the user try to escape the setInputVerifier() by
		 * pressing enter to add the motorcycle (this occurs because there`s no change
		 * of focus in this case).
		 */
		boolean allInputsValid = true;

		if (!Validator.validateFirstNameLastNameCityMakeOrModel(make)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, Verifier.getFirstNameLastNameCityMakeOrModel("make"),
					"Sorry, but the make seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateFirstNameLastNameCityMakeOrModel(model)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, Verifier.getFirstNameLastNameCityMakeOrModel("model"),
					"Sorry, but the model seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateYear(year)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure your year is 4 numbers.",
					"Sorry, but the year seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateSerialNumber(serialNumber)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure your serial number follows the format A12345.",
					"Sorry, but the serial number seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateMileage(mileage)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure that mileage only contains number.",
					"Sorry, but the mileage seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateCustomerId(customerId)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure that your customer ID is an existing one.",
					"Sorry, but the customer ID seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		}
		if (allInputsValid == true) {
			// Creates the new motorcycle
			Motorcycle mot = new Motorcycle.Builder(id).setMake(make).setModel(model).setYear(Integer.valueOf(year))
					.setSerialNumber(serialNumber).setMileage(Integer.valueOf(mileage))
					.setCustomerId(Long.valueOf(customerId)).build();

			try {
				// Adding the updated motorcycle values to the database
				AllData.getMotorcycleDao().update(mot);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}

			// Reloading new Motorcycle Dialog
			properties = new Properties();
			properties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
			db = new Database(properties);
			AllData.loadData(db);
			MotorcycleReportDialog.this.getParent().setVisible(false);
			MotorcycleDialog dialog = new MotorcycleDialog();
			dialog.setVisible(true);
		}
	}

	/**
	 * Deletes the motorcycle from the database.
	 */
	protected void deleteMotorcycle() throws FileNotFoundException, IOException, ApplicationException {
		try {
			AllData.getMotorcycleDao().delete(motorcycle);
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}

		// Reloading new Motorcycle Dialog
		properties = new Properties();
		properties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
		db = new Database(properties);
		AllData.loadData(db);
		MotorcycleReportDialog.this.getParent().setVisible(false);
		MotorcycleDialog dialog = new MotorcycleDialog();
		dialog.setVisible(true);
	}
}
