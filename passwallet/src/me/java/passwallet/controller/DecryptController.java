package me.java.passwallet.controller;

import javax.crypto.BadPaddingException;
import javax.swing.JOptionPane;

import me.java.passwallet.crypto.AESencrp;
import me.java.passwallet.model.PasswordTable;
import me.java.passwallet.view.MainForm;

public class DecryptController {
	
	PasswordTable pwtable = null;
	int targetrow;
	String cipherpw;
	String key;
	String plaintext;
	
	public DecryptController(int row){
		this.targetrow = row;
	}
	
	public void act(MainForm mainform){
		pwtable = mainform.getPasswordTable();
		key = mainform.getJTextField().getText();
		cipherpw = pwtable.getRecordbyRows(targetrow).getpassword();
		
		
		
		//  Check input...
		if(key.length() == 0){
			JOptionPane.showMessageDialog(null, "Please input your wallet password!", "Note", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Decipher...
		AESencrp aes = new AESencrp();
		aes.setALGO("AES");
		byte[] skey = aes.getKey(key);
		try{
			plaintext = aes.decrypt(cipherpw, skey);
		} 
		catch(BadPaddingException bpe){
			JOptionPane.showMessageDialog(null, "Incorrect wallet password!", "Note", JOptionPane.ERROR_MESSAGE);
			mainform.getJTextField().setText("");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "Your Password is:" + plaintext, "Note", JOptionPane.INFORMATION_MESSAGE);
		
		// show pass...
		mainform.getJTextField().setText("");
		mainform.getcontentPane().repaint();
		
		
	}
	
}
