package service;

import tool.MySSLSock;

public class RequestHandlerFactory {
	
	MySSLSock clntsock = null;
	// read,add,change
	
	public  RequestHandlerFactory(MySSLSock clntsock){
		this.clntsock = clntsock;
	}
	
	public Request createHandler(String input){
		String[] request = input.split("\\$SEP\\$");
        String[] args = request[0].split(",");
        String cmd = args[0];
        if(cmd.equals("readDoc")){
        	return new ReadDocRqst(clntsock, args);
        }
        else if(cmd.equals("writeCode")){
        	return new WriteCodeRqst(clntsock, args);
        }
        else if(cmd.equals("addProj")){
        	return new AddProjRqst(clntsock, args);
        }
        else if(cmd.equals("readCode")){
        	return new ReadCodeRqst(clntsock, args);
        }
        else
        	return null;	
	}
	
	
}
