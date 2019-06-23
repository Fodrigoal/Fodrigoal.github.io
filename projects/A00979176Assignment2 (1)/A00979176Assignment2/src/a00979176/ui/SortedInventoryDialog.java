/**
 * Project: A00979176Assignment2
 * File: SortedInventoryDialog.java
 * Date: July 2, 2017
 */

package a00979176.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author Rodrigo Silva, A00971976
 *
 */
@SuppressWarnings("serial")
public class SortedInventoryDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public SortedInventoryDialog(JFrame frame, String report) {
		super(frame, true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Sorted Inventory");
		setSize(720, 350);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow][grow][grow]", "[grow]"));

		JTextArea textArea = new JTextArea();
		contentPanel.add(textArea, "cell 1 0,grow");
		textArea.setText(report);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea.setCaretPosition(0);
		contentPanel.add(scroll);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SortedInventoryDialog.this.dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}
}
