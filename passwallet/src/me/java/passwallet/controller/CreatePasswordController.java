package me.java.passwallet.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import me.java.passwallet.crypto.AESencrp;
import me.java.passwallet.model.PasswordTable;
import me.java.passwallet.model.Record;
import me.java.passwallet.view.AddNewDialog;
import me.java.passwallet.view.MainForm;

public class CreatePasswordController {

	MainForm mainform;
	AddNewDialog addialog;
	PasswordTable pwtable;
	List<Record> recordList = null;
	Record mRecord = null;

	public CreatePasswordController(PasswordTable pwtable) {
		this.pwtable = pwtable;
	}

	public boolean act(AddNewDialog addialog, MainForm mainform) {
		this.addialog = addialog;
		this.mainform = mainform;
		String passwordEnc = null;

		// verify user input...
		String url = addialog.geturl();
		String usrname = addialog.getusrname();
		String password = addialog.gettextFieldpw();
		String wpassword1 = addialog.getpassword1();
		String wpassword2 = addialog.getpassword2();

		if(inputValidationCheck(url, usrname, password, wpassword1, wpassword2) == false){
			return false;
		}
		
//		if(pwtable.checkDuplication(url, usrname) == false){
//			JOptionPane.showMessageDialog(null, " username and url already existed!", "Note", JOptionPane.ERROR_MESSAGE);
//			return false;
//		}

		// encrypt user input...
		AESencrp aes = new AESencrp();
		aes.setALGO("AES");
		byte[] key = aes.getKey(wpassword1);
		try {
			passwordEnc = aes.encrypt(password, key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mRecord = new Record(url, usrname, passwordEnc);

		// refresh file...
		String filePath = mainform.filePath;
		File file = new File(filePath);
		boolean isSame = false;
		if (file.exists()) {
			recordList = FileOption.readFileToList(filePath);
			for (Record mRecord : recordList) {
				if (mRecord.getUrl().equals(url)
						&& mRecord.getUsrname().equals(usrname)) {
					JOptionPane.showMessageDialog(null, " Same url&usrname",
							"Note", JOptionPane.ERROR_MESSAGE);
					isSame = true;
					return false;
				}
			}
			if (!isSame) {
				if (recordList != null) {
					recordList.add(mRecord);
				} else {
					recordList = new ArrayList<Record>();
					recordList.add(mRecord);
				}
				// refresh model...
				pwtable.addNewRecord(url, usrname, passwordEnc.toString());

				// refresh view...
				DefaultTableModel table = (DefaultTableModel) mainform
						.getJTable().getModel();
				String[] set = { url, usrname, passwordEnc.toString() };
				table.addRow(set);

				// refresh GUI...
				mainform.getJTable().repaint();
				mainform.getcontentPane().repaint();

				// write to file
				FileOption.writeListToFile(recordList, filePath);
				// pop out success dialog...
				JOptionPane.showMessageDialog(null, " Success!", "Note",
						JOptionPane.ERROR_MESSAGE);
				
				return true;
			} else {
				return false;
			}
		}
		return false;

	}
	
	
	private boolean inputValidationCheck(String url, String usrname, String password, String wpassword1, String wpassword2){
		if( url.length() == 0 ||  url.length() > 128){
			JOptionPane.showMessageDialog(null, " Invalid url length!\nScale: 0 ~ 128", "Note", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if( usrname.length() == 0 ||  usrname.length() > 50){
			JOptionPane.showMessageDialog(null, " Invalid usrname length!\nScale: 0 ~ 50", "Note", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if( password.length() < 6 ||  password.length() > 26){
			JOptionPane.showMessageDialog(null, " Invalid password length!\nScale: 6 ~ 25", "Note", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if( wpassword1.length() < 6 ||  wpassword1.length() > 26){
			JOptionPane.showMessageDialog(null, " Invalid wallet password length!\nScale: 6 ~ 25", "Note", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if( wpassword1.equals(wpassword2) == false){
			JOptionPane.showMessageDialog(null, " The wallet password is not consistent!", "Note", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		
		
		return true;
		
	}
	
	
}
