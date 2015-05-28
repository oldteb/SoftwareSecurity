package me.java.passwallet.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import me.java.passwallet.controller.DecryptController;
import me.java.passwallet.controller.DeleteRecordController;
import me.java.passwallet.controller.InitController;
import me.java.passwallet.model.PasswordTable;

import javax.swing.JScrollPane;
import javax.swing.JPasswordField;

public class MainForm extends JFrame {

	private JPanel contentPane;
	String[] columnNames = {"URL", "Username", "Password"};
	Object[][] data = null;
	PasswordTable pwtable = new PasswordTable();
	private JTable table;
	public String filePath = "CipherPass.txt";

	
	int currentrow;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm frame = new MainForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainForm() {

		setTitle("Insane Secure Password Wallet");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel passLabel = new JLabel("Password:");
		passLabel.setBounds(10, 226, 70, 17);
		panel.add(passLabel);
		
		JButton decButton = new JButton("Decipher");
		decButton.setBounds(40, 254, 110, 25);
		panel.add(decButton);
		decButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = MainForm.this.table.getSelectedRow();
				new DecryptController(row).act(MainForm.this);				
			}
					
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 404, 192);
		panel.add(scrollPane);
		
		
		DefaultTableModel t = new DefaultTableModel(data,columnNames) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		table = new JTable(t);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		//  Add new record...
		JButton btnNewRecord = new JButton("New Record");
		btnNewRecord.setBounds(160, 254, 110, 25);
		panel.add(btnNewRecord);
		btnNewRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				AddNewDialog newRcdDialog = new AddNewDialog(MainForm.this.pwtable, MainForm.this);
				newRcdDialog.setLocation(newRcdDialog.caculateLocation());
				newRcdDialog.setVisible(true);
			}
					
		});
		
		
		
		
		
		
		//  Delete selected record...
		JButton btnDltRecord = new JButton("DeleteRecord");
		btnDltRecord.setBounds(280, 254, 110, 25);
		panel.add(btnDltRecord);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(74, 225, 316, 17);
		panel.add(passwordField);
		btnDltRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new DeleteRecordController(MainForm.this.pwtable).act(MainForm.this);				
			}
					
		});
		
		new InitController(MainForm.this.pwtable).act(MainForm.this); 
		
		
	}
	
	
	public JTable getJTable(){
		return this.table;
	}
	
	public PasswordTable getPasswordTable(){
		return  this.pwtable;
	}
	
	public JTextField getJTextField(){
		return  this.passwordField;
	}
	
	public JPanel getcontentPane(){
		return  this.contentPane;
	}
	

}
