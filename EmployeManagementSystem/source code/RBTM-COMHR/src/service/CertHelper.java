package service;

import tool.MySSLSRVSock;
import tool.SigGenerator;

public class CertHelper {
	
	MySSLSRVSock srv_socket = null;
	SigGenerator sg = null;
	
	public CertHelper(MySSLSRVSock srv_socket){
		this.srv_socket = srv_socket;
		this.sg = new SigGenerator(srv_socket);
	}
	
	public String getFacts(String msg) throws Exception{
		String[] temp_msg = msg.split("\\$SEP\\$");
		String facts = "";
		
		for(int i = 1; i < temp_msg.length ; i++){
			if(verify(temp_msg[i]) == true){
				facts += "$SEP$" + interprete(temp_msg[i]);
			}
		}
		
		return facts;
		
	}
	
	public String getRequest(String msg) throws Exception{
		String[] temp_msg = msg.split("\\$SEP\\$");
		return temp_msg[0];
		
	}
	
	private Boolean verify(String rowmsg) throws Exception{
		int len = Integer.parseInt(rowmsg.substring(0, 3));
		String source = rowmsg.substring(3, 8);
		String context = (String)rowmsg.substring(8, len);
		String cipher = (String)rowmsg.substring(len);
		String hashFromDec = sg.decryptByPublicKey(cipher,source);
		String hashFromMsg = sg.getHash(rowmsg.substring(0, len));
		if(hashFromDec.equals(hashFromMsg)){
			return true;
		}
		return false;
	}
	
	private String interprete(String rowmsg){
		int len = Integer.parseInt(rowmsg.substring(0, 3));
		String context = (String)rowmsg.substring(8, len);
		return context;
	}
	
	String sign(String msg) throws Exception{
		String temp = "COMHR" + msg;
		int len = temp.length() + 3;
		String s_len = String.valueOf(len);
		for(int i = s_len.length(); i < 3 ; i++){
			s_len = "0" + s_len;
		}
		String newmsg = s_len + temp;
		newmsg += sg.encryptByPrivateKey(sg.getHash(newmsg));
		
		return newmsg;
	}
	
}
