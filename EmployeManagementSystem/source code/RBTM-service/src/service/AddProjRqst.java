package service;

import java.io.IOException;

import Main.Main;
import tool.MySSLSock;
import exception.InvalidParameterException;
import file.FileList;

public class AddProjRqst implements Request{
	String[] args = null;
	FileList tfl = null;
	MySSLSock clntsock = null;
	
	AddProjRqst(MySSLSock socket, String[] args){
		this.args = args;
		tfl = Main.fl;
		this.clntsock = socket;
	}

	@Override
	public void act() throws InvalidParameterException, IOException{

		if(args.length != 3){
			throw new InvalidParameterException("expecting 2 args, but recving " + (args.length-1));
		}
		else{
			if(tfl.get(args[1]) != null){
				throw new InvalidParameterException("Project already existed.");
			}
			else{
				tfl.add(args[1], args[2]);
				clntsock.send_byte("Success.");
			}
		}
	}
	
	
}