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
import a00979176.data.Motorcycle;
import net.miginfocom.swing.MigLayout;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
@SuppressWarnings("serial")
public class MotorcycleDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public static final String HORIZONTAL_LINE = "-----------------------------------------------------------------------------------------------";

	@SuppressWarnings("unchecked")
	public MotorcycleDialog() {
		setSize(730, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Motorcycle Report");
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow][grow][grow]", "[grow]"));

		// Setting the motorcycles to show at the Dialog
		{
			// Creating a motorcycle List and adding it to a DefaultListModel
			List<Motorcycle> motorcycle = new ArrayList<Motorcycle>();
			motorcycle.addAll(AllData.getMotorcycles().values());
			@SuppressWarnings("rawtypes")
			DefaultListModel listModel = new DefaultListModel();
			listModel.addElement(HORIZONTAL_LINE);
			listModel.addElement(String.format("%-7s %-29s %-24s %13s %18s", "Year", "Make", "Model",
					"Mileage", "Customer ID"));
			listModel.addElement(HORIZONTAL_LINE);
			for (Motorcycle cus : motorcycle) {
				listModel.addElement(cus);
			}
			// Passing the motorcycles DefaultListModel to a JList
			JList<Motorcycle> list = new JList<Motorcycle>(listModel);
			list.setFont(new Font("Courier New", Font.PLAIN, 12));
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setLayoutOrientation(JList.VERTICAL);
			// Adding mouse events to JList
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent evt) {
					JList<Motorcycle> listModel = (JList<Motorcycle>) evt.getSource();

					if (evt.getClickCount() == 2) {
						// Creates a Dialog where the user can update or delete the motorcycle
						MotorcycleReportDialog dialog = new MotorcycleReportDialog(listModel.getSelectedValue());
						dialog.setVisible(true);
					}
				}
			});
			
			// Create JScrollPane with the motorcycles JList
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(list);
			contentPanel.add(scrollPane, "flowy,cell 1 0,grow");

			// Create new Panel for the buttons
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				// Add button
				JButton addButton = new JButton("Add Motorcycle");
				addButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Creates a Dialog where the user can add a motorcycle
						MotorcycleAddDialog dialog = new MotorcycleAddDialog();
						dialog.setVisible(true);
					}
				});
				addButton.setActionCommand("Add Motorcycle");
				buttonPane.add(addButton);

				// Create the "OK" button and it's listener
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						MotorcycleDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);

				// Closes the window when the 'Esc' key is pressed.
				KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
				Action escapeAction = new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						MotorcycleDialog.this.dispose();
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
