/**
 * Project: A00979176Assignment2
 * File: MotorcycleDialog.java
 * Date: July 2, 2017
 */

package a00979176.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import a00979176.data.AllData;
import a00979176.data.Customer;
import net.miginfocom.swing.MigLayout;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
@SuppressWarnings("serial")
public class CustomerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public static final String HORIZONTAL_LINE = "-----------------------------------------------------------------------------------------------------------------------------------";

	@SuppressWarnings("unchecked")
	public CustomerDialog() {
		setSize(990, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Customer Report");
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow][grow][grow]", "[grow]"));

		// Setting the customers to show at the Dialog
		{
			// Creating a customer List and adding it to a DefaultListModel
			List<Customer> customer = new ArrayList<Customer>();
			customer.addAll(AllData.getCustomers().values());
			@SuppressWarnings("rawtypes")
			DefaultListModel listModel = new DefaultListModel();
			listModel.addElement(HORIZONTAL_LINE);
			listModel.addElement(String.format("%-7s %-44s %-37s %13s %26s", "Id", "Last Name, First Name", "Email",
					"Phone", "Joined Date"));
			listModel.addElement(HORIZONTAL_LINE);
			for (Customer cus : customer) {
				listModel.addElement(cus);
			}
			// Passing the customers DefaultListModel to a JList
			JList<Customer> list = new JList<Customer>(listModel);
			list.setFont(new Font("Courier New", Font.PLAIN, 12));
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setLayoutOrientation(JList.VERTICAL);
			// Adding mouse events to JList
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent evt) {
					JList<Customer> listModel = (JList<Customer>) evt.getSource();

					if (evt.getClickCount() == 2) {
						// Creates a Dialog where the user can update or delete the customer
						CustomerReportDialog dialog = new CustomerReportDialog(listModel.getSelectedValue());
						dialog.setVisible(true);
					}
				}
			});
			
			// Create JScrollPane with the customers JList
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(list);
			contentPanel.add(scrollPane, "flowy,cell 1 0,grow");

			// Create new Panel for the buttons
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				// Add button
				JButton addButton = new JButton("Add Customer");
				addButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Creates a Dialog where the user can add a customer
						CustomerAddDialog dialog = new CustomerAddDialog();
						dialog.setVisible(true);
					}
				});
				addButton.setActionCommand("Add Customer");
				buttonPane.add(addButton);

				// Create the "OK" button and it's listener
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						CustomerDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);

				// Closes the window when the 'Esc' key is pressed.
				KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
				Action escapeAction = new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						CustomerDialog.this.dispose();
					}
				};
				getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
				getRootPane().getActionMap().put("ESCAPE", escapeAction);

				// Setting the default button
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
