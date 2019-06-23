/**
 * Project: A00979176Assignment2
 * File: InventoryReportDialog.java
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
import a00979176.data.Inventory;
import a00979176.data.util.Validator;
import a00979176.database.Database;
import net.miginfocom.swing.MigLayout;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
@SuppressWarnings("serial")
public class InventoryAddDialog extends JDialog {

	private Database db;
	private Properties properties;
	public static final String DB_PROPERTIES_FILENAME = "db.properties";
	private static Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField partNumberField;
	private JTextField motorcycleIdField;
	private JTextField descriptionField;
	private JTextField priceField;
	private JTextField quantityField;

	public InventoryAddDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(450, 250);
		setLocationRelativeTo(null);
		setTitle("Inventory Details");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][]"));

		// Creating the Inventory Number field and it's value
		JLabel lblPartNumber = new JLabel("Inventory Number");
		contentPanel.add(lblPartNumber, "cell 0 0,alignx trailing");
		partNumberField = new JTextField();
		partNumberField.setEnabled(false);
		partNumberField.setEditable(false);
		contentPanel.add(partNumberField, "cell 1 0,growx");
		partNumberField.setColumns(10);
		partNumberField.setText(Long.toString(getMaxInventoryNumber() + 1));

		// Creating the Motorcycle ID field and it's validator
		JLabel lblMotorcycleId = new JLabel("Motorcycle+Maker");
		contentPanel.add(lblMotorcycleId, "cell 0 1,alignx trailing");
		motorcycleIdField = new JTextField();
		contentPanel.add(motorcycleIdField, "cell 1 1,growx");
		motorcycleIdField.setColumns(10);
		motorcycleIdField.setInputVerifier(new Verifier("motorcycleId"));

		// Creating the Description field and it's validator
		JLabel lblDescription = new JLabel("Description");
		contentPanel.add(lblDescription, "cell 0 2,alignx trailing");
		descriptionField = new JTextField();
		contentPanel.add(descriptionField, "cell 1 2,growx");
		descriptionField.setColumns(10);
		descriptionField.setInputVerifier(new Verifier("description"));

		// Creating the Price field and it's validator
		JLabel lblPrice = new JLabel("Price");
		contentPanel.add(lblPrice, "cell 0 3,alignx trailing");
		priceField = new JTextField();
		contentPanel.add(priceField, "cell 1 3,growx");
		priceField.setColumns(10);
		priceField.setInputVerifier(new Verifier("price"));

		// Creating the Quantity field and it's validator
		JLabel lblQuantity = new JLabel("Quantity");
		contentPanel.add(lblQuantity, "cell 0 4,alignx trailing");
		quantityField = new JTextField();
		contentPanel.add(quantityField, "cell 1 4,growx");
		quantityField.setColumns(10);
		quantityField.setInputVerifier(new Verifier("quantity"));

		// Create new Panel for the buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		// Create the "Add Inventory" button and it's listener
		JButton addButton = new JButton("Add Inventory");
		addButton.setBackground(Color.GREEN);
		addButton.setForeground(Color.BLACK);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					AddInventory();
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
		addButton.setActionCommand("Add Inventory");
		buttonPane.add(addButton);

		// Create the "Cancel" button and it's listener
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setVerifyInputWhenFocusTarget(false);
		cancelButton.setBackground(Color.YELLOW);
		cancelButton.setForeground(Color.BLACK);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InventoryAddDialog.this.getParent().setVisible(false);
				InventoryAddDialog.this.dispose();
				InventoryDialog dialog = new InventoryDialog();
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
				InventoryAddDialog.this.dispose();
			}
		};
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
		getRootPane().getActionMap().put("ESCAPE", escapeAction);

		// Setting the default button
		getRootPane().setDefaultButton(addButton);
	}

	/**
	 * Validate the new inventory and adds it to the database if approved.
	 */
	protected void AddInventory() throws FileNotFoundException, IOException, ApplicationException {
		String partNumber = partNumberField.getText();
		String motorcycleId = motorcycleIdField.getText();
		String description = descriptionField.getText();
		// Remove the cents
		String price = priceField.getText().split("\\.", 2)[0];
		String quantity = quantityField.getText();

		/*
		 * Validation checks in case the user try to escape the setInputVerifier() by
		 * pressing enter to add the inventory (this occurs because there`s no change of
		 * focus in this case).
		 */
		boolean allInputsValid = true;

		if (!Validator.validateMotorcycleId(motorcycleId)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure that motorcycle+maker follows the format Make+Model.",
					"Sorry, but the inventory number seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateDescription(description)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure that description is less than 55 characters.",
					"Sorry, but the description number seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
	}else if (!Validator.validatePrice(price)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null,
					"Please ensure that price is only numbers. No cents necessary.",
					"Sorry, but the price seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		} else if (!Validator.validateQuantity(quantity)) {
			allInputsValid = false;
			JOptionPane.showMessageDialog(null, "Please ensure that quantity only contains numbers and has less than 9 digits.",
					"Sorry, but the quantity seems to be incorrect", JOptionPane.INFORMATION_MESSAGE);
		}

		if (allInputsValid == true) {
			// Creates the new inventory
			Inventory mot = new Inventory.Builder(partNumber).setMotorcycleId(motorcycleId).setDescription(description).setPrice(Integer.valueOf(price))
					.setQuantity(Integer.valueOf(quantity)).build();

			try {
				// Adding the created inventory to the database
				AllData.getInventoryDao().add(mot);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}

			// Reloading new Inventory Dialog
			properties = new Properties();
			properties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
			db = new Database(properties);
			AllData.loadData(db);
			this.getParent().setVisible(false);
			InventoryDialog dialog = new InventoryDialog();
			dialog.setVisible(true);
		}
	}

	/**
	 * @return the max Inventory Number
	 */
	public long getMaxInventoryNumber() {
		List<Inventory> inventory = new ArrayList<Inventory>();
		inventory.addAll(AllData.getInventory().values());
		long maxId = 0;

		for (Inventory mot : inventory) {
			long id = Long.valueOf(mot.getPartNumber());
			if (id > maxId) {
				maxId = id;
			}
		}
		return maxId;
	}
	
}
