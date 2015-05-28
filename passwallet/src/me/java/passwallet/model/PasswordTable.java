package me.java.passwallet.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.java.passwallet.controller.FileOption;

public class PasswordTable {
	List<Record> recordList = new ArrayList<Record>();
	
	public int size(){
		return recordList.size();
	}
	
	public List<Record> getRecordList() {
		return recordList;
	}

	public Record getRecordbyRows(int row){
		return recordList.get(row);
	}
	
	public boolean addNewRecord(String url, String usrname, String passwordEnc){
		Record newRecord = new Record(url, usrname, passwordEnc);
		if(recordList.add(newRecord)){
			return true;
		}
		else
			return false;
	}
	
	public void removeRecord(int row){
		recordList.remove(row);
	}
	
	public boolean loadFromFile(File file) {
		if(file.exists()){
			recordList = FileOption.readFileToList(file.getAbsolutePath());
		}else{
			return false;
		}
		return true;
	}
	
	public boolean checkDuplication(String url, String usrname){
		for(int i = 0; i < recordList.size(); i++){
			if(recordList.get(i).getUrl() == url && recordList.get(i).getUsrname() == usrname){
				return false;
			}
		}
		
		return true;
			
	}
}
