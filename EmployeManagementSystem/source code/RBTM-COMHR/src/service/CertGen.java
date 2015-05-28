package service;

import java.util.ArrayList;
import java.util.List;

import exception.OperationUnauthorizedException;
import exception.PrologExecutionException;
import prolog.PrologEngine;
import prolog.PrologQuery;
import tool.MySSLSRVSock;

public class CertGen {
	
	String peerid = null;
	MySSLSRVSock srv_socket = null;
	String[] cmdlib = {"comEmployee", "manager", "engineer"};
	
	public CertGen(String id, MySSLSRVSock srv_socket){
		this.peerid = id;
		this.srv_socket = srv_socket;
	}
	
	
	public String getCerts(String msg) throws Exception{
		CertHelper cer = new CertHelper(srv_socket);
		String facts = cer.getFacts(msg);
		if(facts == null){
			facts = "";
		}
		
		PrologQuery query = null;
		String temp = "";
		List<String> list = new ArrayList<String>();
		
		for( String cmd : cmdlib){
			temp = cmd + facts;
			query = new PrologQuery(peerid, temp);
			if(PrologEngine.getInstance().execute(query) == true){
				list.add(cmd);
			}
		}
		if(list.size() == 0){
			return "None";
		}
		else{
			temp = "";
			for(String str : list){
				String t_str = str + "(" + peerid + ")";
				temp += "$SEP$" + cer.sign(t_str);
				//System.out.println(temp);
			}
		}
		return temp;
	}
	
	
	
}
