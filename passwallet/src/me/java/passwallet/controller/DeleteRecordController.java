package me.java.passwallet.controller;

import javax.crypto.BadPaddingException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import me.java.passwallet.crypto.AESencrp;
import me.java.passwallet.model.PasswordTable;
import me.java.passwallet.view.MainForm;

public class DeleteRecordController {

	int targetrow;
	String cipherpw;
	String key;
	
	PasswordTable pwtable = null;
	
	public DeleteRecordController(PasswordTable pwtable){
		this.pwtable = pwtable;
	}
	
	public void act(MainForm mainform){
		//  get the current Record...
		targetrow = mainform.getJTable().getSelectedRow();
		cipherpw = pwtable.getRecordbyRows(targetrow).getpassword();
			
		//  get the input...
		key = mainform.getJTextField().getText();
		// Decipher...
		AESencrp aes = new AESencrp();
		aes.setALGO("AES");
		
		//  generate the skey.
		byte[] skey = aes.getKey(key);
		
		//  verify the key
		try{
			aes.decrypt(cipherpw, skey);
		} 
		catch(BadPaddingException bpe){
			JOptionPane.showMessageDialog(null, " Incorrect password!", "Note", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, " Incorrect password!", "Note", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		
		//  refresh the table...
		DefaultTableModel table = (DefaultTableModel)mainform.getJTable().getModel();
		table.removeRow(targetrow);
		
		//  refresh the passwordtable...
		pwtable.removeRecord(targetrow);
		
		//  if success....refresh the file...
		FileOption.writeListToFile(pwtable.getRecordList(), mainform.filePath);
		
		//  refresh GUI...
		mainform.getJTextField().setText("");
		mainform.getJTable().repaint();
		mainform.getcontentPane().repaint();
	}
}
