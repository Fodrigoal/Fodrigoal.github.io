/**
 * Project: A00979176Assignment2
 * File: MainFrame.java
 * Date: July 2, 2017
 */

package a00979176.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00979176.data.AllData;
import a00979176.data.Inventory;
import a00979176.data.Motorcycle;
import a00979176.io.InventoryReport;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static final Logger LOG = LogManager.getLogger();
	private JPanel contentPane;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 300);
		setLocationRelativeTo(null);
		setTitle("BCMC");

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK));
		mntmQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LOG.info("Quit menu item pressed.");
				System.exit(0);
			}
		});
		mnFile.add(mntmQuit);

		JMenu mnData = new JMenu("Data");
		mnData.setMnemonic(KeyEvent.VK_D);
		menuBar.add(mnData);

		JMenuItem mntmCustomer = new JMenuItem("Customers");
		mntmCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LOG.info("Customer menu item pressed.");
				JOptionPane.showMessageDialog(MainFrame.this, "This feature is not available yet.", "Customer Report",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mntmCustomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
		mnData.add(mntmCustomer);

		JMenuItem mntmService = new JMenuItem("Service");
		mntmService.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LOG.info("Service menu item pressed.");
				List<Motorcycle> motorcycle = new ArrayList<Motorcycle>();
				motorcycle.addAll(AllData.getMotorcycles().values());
			}
		});
		mntmService.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
		mnData.add(mntmService);

		JMenuItem mntmInventory = new JMenuItem("Inventory");
		mntmInventory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LOG.info("Inventory menu item pressed.");
				List<Inventory> inventory = new ArrayList<Inventory>();
				inventory.addAll(AllData.getInventory().values());
				InventoryDialog dialog = new InventoryDialog(inventory);
				dialog.setVisible(true);
			}
		});
		mntmInventory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_MASK));
		mnData.add(mntmInventory);

		JMenu mnReports = new JMenu("Reports");
		mnReports.setMnemonic(KeyEvent.VK_R);
		menuBar.add(mnReports);

		JMenuItem mntmTotal = new JMenuItem("Total");
		mntmTotal.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK));
		mntmTotal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LOG.info("Total menu item pressed.");
				JOptionPane.showMessageDialog(MainFrame.this, InventoryReport.generateTotalInventory(), "Total Report",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnReports.add(mntmTotal);

		JSeparator separator_2 = new JSeparator();
		mnReports.add(separator_2);

		JCheckBoxMenuItem chckbxmntmDescending = new JCheckBoxMenuItem("Descending");
		chckbxmntmDescending.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
		mnReports.add(chckbxmntmDescending);

		JSeparator separator_3 = new JSeparator();
		mnReports.add(separator_3);

		JMenuItem mntmByDescription = new JMenuItem("By Description");
		mntmByDescription.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
		mntmByDescription.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LOG.debug("By Description menu item pressed.");
				String output = InventoryReport.generateInventoryUIReport(null, chckbxmntmDescending.isSelected(),
						"description");
				SortedInventoryDialog dialog = new SortedInventoryDialog(MainFrame.this, output);
				dialog.setVisible(true);
			}
		});
		mnReports.add(mntmByDescription);

		JMenuItem mntmByCount = new JMenuItem("By Count");
		mntmByCount.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_MASK));
		mntmByCount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LOG.debug("By Count menu item pressed.");
				String output = InventoryReport.generateInventoryUIReport(null, chckbxmntmDescending.isSelected(),
						"count");
				SortedInventoryDialog dialog = new SortedInventoryDialog(MainFrame.this, output);
				dialog.setVisible(true);
			}
		});
		mnReports.add(mntmByCount);

		JMenuItem mntmMake = new JMenuItem("Make");
		mntmMake.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LOG.debug("Make menu item pressed.");
				String make = JOptionPane.showInputDialog(MainFrame.this, "Please, input the maker:", "Filter by maker",
						JOptionPane.OK_CANCEL_OPTION);
				String output = InventoryReport.generateInventoryUIReport(make, chckbxmntmDescending.isSelected(),
						null);
				if (output.isEmpty()) {
					JOptionPane.showMessageDialog(MainFrame.this, "Invalid make", "Invalid Input",
							JOptionPane.WARNING_MESSAGE);
				} else if (make != null) {
					SortedInventoryDialog dialog = new SortedInventoryDialog(MainFrame.this, output);
					dialog.setVisible(true);
				}
			}
		});
		mntmMake.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_MASK));
		mnReports.add(mntmMake);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, "BCMC\nBy Rodrigo Silva\nA00979176", "About BCMC",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
}
