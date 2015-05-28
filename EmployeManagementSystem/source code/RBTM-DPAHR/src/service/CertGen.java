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
	String[] cmdlib = {"employee", "head"};
	
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
		String temp = null;
		List<String> list = new ArrayList<String>();
		
		for( String cmd : cmdlib){
			temp = cmd + facts;
			query = new PrologQuery(peerid, "Variable", temp);
			if(PrologEngine.getInstance().execute(query) == true){
				list.add(query.getResult());
			}
		}
		if(list.size() == 0){
			return "";
		}
		else{
			temp = "";
			for(String str : list){
				//String t_str = str + "(" + peerid + ")";
				temp += "$SEP$" + cer.sign(str);
				//System.out.println(temp);
			}
		}
		return temp;
	}
	
	
	
}
