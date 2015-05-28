package me.java.passwallet.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import me.java.passwallet.controller.CreatePasswordController;
import me.java.passwallet.model.PasswordTable;

public class AddNewDialog extends JDialog{

	private final JPanel contentPanel = new JPanel();
	private JTextField urlTextField;
	private JTextField usrnameTextField;
	private JPasswordField passwordField1;
	private JPasswordField passwordField2;
	
	PasswordTable pwtable;
	MainForm mainform;
	private JTextField textFieldpw;
	

	/**
	 * Create the dialog.
	 */
	public AddNewDialog(PasswordTable pwtable, MainForm mainform) {
		// set action permit on current dialog only...
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		
		// Set PasswordTable and mainform...
		this.pwtable = pwtable;
		this.mainform = mainform;
		
		// Content setup...
		setTitle("Add New Record");
		setBounds(100, 100, 450, 308);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 414, 247);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("URL");
		lblNewLabel.setBounds(30, 30, 65, 20);
		panel.add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(30, 65, 65, 20);
		panel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(30, 100, 65, 20);
		panel.add(lblPassword);
		
		JLabel lblComfirmPasswpard = new JLabel("Wallet Passwpard");
		lblComfirmPasswpard.setBounds(30, 135, 130, 20);
		panel.add(lblComfirmPasswpard);
		
		urlTextField = new JTextField();
		urlTextField.setBounds(160, 30, 183, 20);
		panel.add(urlTextField);
		urlTextField.setColumns(10);
		
		usrnameTextField = new JTextField();
		usrnameTextField.setColumns(10);
		usrnameTextField.setBounds(160, 65, 183, 20);
		panel.add(usrnameTextField);
		
		passwordField1 = new JPasswordField();
		passwordField1.setBounds(160, 135, 183, 20);
		panel.add(passwordField1);
		
		passwordField2 = new JPasswordField();
		passwordField2.setBounds(160, 168, 183, 20);
		panel.add(passwordField2);
		
		
		final JButton okButton = new JButton("OK");

		okButton.setBounds(96, 213, 89, 23);
		panel.add(okButton);
		
		
		final JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(222, 213, 89, 23);
		panel.add(cancelButton);
		
		JLabel label = new JLabel("Comfirm Passwpard");
		label.setBounds(30, 166, 130, 20);
		panel.add(label);
		
		textFieldpw = new JTextField();
		textFieldpw.setColumns(10);
		textFieldpw.setBounds(160, 100, 183, 20);
		panel.add(textFieldpw);
		
		
		ActionListener lst = new ActionListener() {         
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == okButton){
                	//  submit request here...
                	CreatePasswordController capc = new CreatePasswordController(AddNewDialog.this.pwtable);
                	if(capc.act(AddNewDialog.this, AddNewDialog.this.mainform) == false){
                		setContent("","","","","");	
                	}
                	else{
                    	setVisible(false);
                        dispose();
                	}
                }                
                else if(e.getSource() == cancelButton){
                	//  Return to mainform...
                	setVisible(false);
                    dispose();
                }
                
            }
        };
        
        okButton.addActionListener(lst);
        cancelButton.addActionListener(lst);

		

	}
	
	// Calculate owner
    public Point caculateLocation() {
    	int ownerWidth = getOwner().getWidth();
    	int ownerHeight = getOwner().getHeight();
    	
        Point ownerLocation = getOwner().getLocation();
        Dimension ownerSize = getOwner().getSize();
        //Get x and y by geometry relationship
        double x = 0.5 * ownerSize.getWidth() +
                ownerLocation.getX() - 0.5 * ownerWidth;
        double y = 0.5 * ownerSize.getHeight() +
                ownerLocation.getY() - 0.5 * ownerHeight;
        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().height;
        //Make the dialog display in the area of screen
        if (x < 1) {
            x = 1;
        }
        if (y < 1) {
            y = 1;
        }
        if (x > screenWidth - this.getWidth()) {
            x = screenWidth - this.getWidth();
        }
        if (y > screenHeight - this.getHeight()) {
            y = screenHeight - this.getHeight();
        }
        return new Point((int) x, (int) y);
    }
    

    public String geturl(){
    	return this.urlTextField.getText();
    }
    
    public String getusrname(){
    	return this.usrnameTextField.getText();
    }
    
    public String gettextFieldpw(){
    	return this.textFieldpw.getText();
    }
    
    public String getpassword1(){
    	return this.passwordField1.getText();
    }
    
    public String getpassword2(){
    	return this.passwordField2.getText();
    }
    
    public void setContent(String url, String usrname, String password, String wpassword1, String wpassword2){
    	urlTextField.setText(url);
    	usrnameTextField.setText(usrname);
    	textFieldpw.setText(password);
    	passwordField1.setText(wpassword1);
    	passwordField2.setText(wpassword2);

    }    
    
    
    
//    public void actionPerformed(ActionEvent e) {
//        dispose();
//    }
    
//    private void Dispose(){
//    	AddNewDialog.this.set
//    }
//    
//    private void enableOwner(){
//    	this.getOwner().setEnabled(true);
//    }
}
