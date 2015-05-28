package me.java.passwallet.controller;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import me.java.passwallet.model.PasswordTable;
import me.java.passwallet.model.Record;
import me.java.passwallet.view.MainForm;

public class InitController {
	
	PasswordTable pwtable;
	
	public InitController(PasswordTable pwtable){
		this.pwtable = pwtable;
	}
	
	
	public void act(MainForm mainform){
		
		if(pwtable.loadFromFile(new File(mainform.filePath)) != true ){
			JOptionPane.showMessageDialog(null, " File not found!", "Note", JOptionPane.ERROR_MESSAGE);	
		}
		
		// Refresh the table...
		int s = pwtable.size();
		DefaultTableModel table = (DefaultTableModel)mainform.getJTable().getModel();
		int ts = table.getRowCount();
		//System.out.println(ts);
		if(ts > 0){
			// Clear the table...
			for(int i = 0; i < ts; i++){
				table.removeRow(0);
			}
		}
		
		Record rd;

		// Add records...
		for(int i = 0; i < s; i++){
			rd = pwtable.getRecordbyRows(i);
			String[] set = {rd.getUrl(), rd.getUsrname(), rd.getpassword()};
         	table.addRow(set);
		}
			
		// Refresh the graph.
		mainform.getJTable().setModel(table);
		mainform.getJTable().repaint();
		
	}
	
}
