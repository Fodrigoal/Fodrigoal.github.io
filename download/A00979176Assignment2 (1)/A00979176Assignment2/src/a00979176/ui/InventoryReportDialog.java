/**
 * Project: A00979176Assignment2
 * File: InventoryReportDialog.java
 * Date: July 2, 2017
 */

package a00979176.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00979176.data.AllData;
import a00979176.data.Inventory;
import net.miginfocom.swing.MigLayout;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
@SuppressWarnings("serial")
public class InventoryReportDialog extends JDialog {

	private static Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField descriptionField;
	private JTextField partNumberField;
	private JTextField priceField;
	private JTextField quantityField;
	private JTextField motorcycleIdField;
	private Inventory inventory;

	public InventoryReportDialog(Inventory inventory) {
		this.inventory = inventory;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(450, 250);
		setLocationRelativeTo(null);
		setTitle("Part Details");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][]"));

		JLabel lblPartNumber = new JLabel("Part Number");
		contentPanel.add(lblPartNumber, "cell 0 0,alignx trailing");

		partNumberField = new JTextField();
		partNumberField.setEnabled(false);
		partNumberField.setEditable(false);
		contentPanel.add(partNumberField, "cell 1 0,growx");
		partNumberField.setColumns(10);

		JLabel lblDescription = new JLabel("Description");
		contentPanel.add(lblDescription, "cell 0 1,alignx trailing");

		descriptionField = new JTextField();
		contentPanel.add(descriptionField, "cell 1 1,growx");
		descriptionField.setColumns(10);

		JLabel lblmotorcycleId = new JLabel("Motorcycle Id");
		contentPanel.add(lblmotorcycleId, "cell 0 2,alignx trailing");

		motorcycleIdField = new JTextField();
		contentPanel.add(motorcycleIdField, "cell 1 2,growx");
		motorcycleIdField.setColumns(10);

		JLabel lblPrice = new JLabel("Price");
		contentPanel.add(lblPrice, "cell 0 3,alignx trailing");

		priceField = new JTextField();
		contentPanel.add(priceField, "cell 1 3,growx");
		priceField.setColumns(10);

		JLabel lblQuantity = new JLabel("Quantity");
		contentPanel.add(lblQuantity, "cell 0 4,alignx trailing");

		quantityField = new JTextField();
		contentPanel.add(quantityField, "cell 1 4,growx");
		quantityField.setColumns(10);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateInventory();
				InventoryReportDialog.this.dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InventoryReportDialog.this.dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		setInventory(inventory);
	}

	public void setInventory(Inventory inventory) {
		partNumberField.setText(inventory.getPartNumber());
		descriptionField.setText(inventory.getDescription());
		motorcycleIdField.setText(inventory.getMotorcycleId());
		priceField.setText(Integer.toString(inventory.getPrice()));
		quantityField.setText(Integer.toString(inventory.getQuantity()));
	}

	protected void updateInventory() {
		inventory.setDescription(descriptionField.getText());
		inventory.setMotorcycleId(motorcycleIdField.getText());
		inventory.setPrice(Integer.valueOf(priceField.getText()));
		inventory.setQuantity(Integer.valueOf(quantityField.getText()));
		try {
			AllData.getInventoryDao().update(inventory);
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
	}

	public Inventory getInventory() {
		return inventory;
	}
}
