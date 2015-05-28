package author;

import exception.PrologExecutionException;
import prolog.PrologEngine;
import prolog.PrologQuery;
import tool.MySSLSRVSock;

public class Authorizer {
	
	MySSLSRVSock srv_socket = null;
	
	public Authorizer(MySSLSRVSock srv_socket){	
		this.srv_socket = srv_socket;
	}
	
	
	public Boolean deal(String peerid, String msg) throws PrologExecutionException, Exception {
		CertHelper cer = new CertHelper(srv_socket);
		String facts = cer.getFacts(msg);
		if(facts == null){
			facts = "";
		}
		String temp = cer.getRequest(msg) + facts;
		PrologQuery query = new PrologQuery(peerid, temp);
		if(PrologEngine.getInstance().execute(query) == true){
			return true;
		}
		return false;
	}
		
	
}
