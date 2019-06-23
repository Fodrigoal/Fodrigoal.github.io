/**
 * Project: A00979176Assignment2
 * File: InventoryDialog.java
 * Date: July 2, 2017
 */

package a00979176.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import a00979176.data.Inventory;
import net.miginfocom.swing.MigLayout;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
@SuppressWarnings("serial")
public class InventoryDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public InventoryDialog(Collection<Inventory> inventoryParts) {
		setSize(550, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Inventory Report");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[28.00][500.00][grow]", "[grow]"));
		{
			DefaultListModel<Inventory> listModel = new DefaultListModel<Inventory>();
			for (Inventory inv : inventoryParts) {
				listModel.addElement(inv);
			}
			JList<Inventory> list = new JList<Inventory>(listModel);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setLayoutOrientation(JList.VERTICAL);
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent evt) {
					@SuppressWarnings("unchecked")
					JList<Inventory> list = (JList<Inventory>) evt.getSource();

					if (evt.getClickCount() == 2) {
						InventoryReportDialog dialog = new InventoryReportDialog(list.getSelectedValue());
						dialog.setVisible(true);
					}
				}
			});
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(list);
			contentPanel.add(scrollPane, "flowy,cell 1 0,grow");
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						InventoryDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
