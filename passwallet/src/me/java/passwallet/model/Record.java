package me.java.passwallet.model;

import java.io.Serializable;

public class Record implements Serializable{

	private String url;
	private String usrname;
	private String password;
	
	
	public Record(String url, String usrname, String password){
		this.url = url;
		this.usrname = usrname;
		this.password = password;
	}
	
	
	public String setUrl(String url){
		return this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String setUsrname(String usrname){
		return this.usrname = usrname;
	}
	
	public String getUsrname(){
		return usrname;
	}
	
	public String setpassword(String password){
		return this.password = password;
	}
	
	public String getpassword(){
		return password;
	}
	
	public String toString(){
		return new String(url + "," + usrname + ","+ password);
	}
		
		
}
