package service;

import java.io.IOException;

import Main.Main;
import tool.MySSLSock;
import exception.InvalidParameterException;
import file.FileList;

public class WriteCodeRqst implements Request{
	String[] args = null;
	FileList tfl = null;
	MySSLSock clntsock = null;
	
	WriteCodeRqst(MySSLSock socket, String[] args){
		this.args = args;
		tfl = Main.fl;
		this.clntsock = socket;
	}

	
	
	@Override
	public void act() throws InvalidParameterException, IOException{

		if(args.length != 3){
			throw new InvalidParameterException("expecting 2 arg, but recving " + (args.length-1));
		}
		else if(Integer.parseInt(args[1]) < 100){
			throw new InvalidParameterException("Code index range from 100+");
		}
		else{
			if(tfl.get(args[1]) == null){
				throw new InvalidParameterException("Record not found.");
			}
			else{
				tfl.add(args[1], args[2]);
				clntsock.send_byte("Success.");
			}
		}
	}
	
}