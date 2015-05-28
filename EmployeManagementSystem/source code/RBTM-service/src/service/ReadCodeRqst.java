package service;

import java.io.IOException;

import tool.MySSLSock;
import Main.Main;
import exception.InvalidParameterException;
import file.FileList;

public class ReadCodeRqst implements Request{
	String[] args = null;
	FileList tfl = null;
	MySSLSock clntsock = null;
	
	ReadCodeRqst(MySSLSock socket, String[] args){
		this.args = args;
		tfl = Main.fl;
		this.clntsock = socket;
	}

	@Override
	public void act() throws InvalidParameterException, IOException{

		if(args.length != 2){
			throw new InvalidParameterException("expecting 1 arg, but recving " + (args.length-1));
		}
		else if(Integer.parseInt(args[1]) < 100){
			throw new InvalidParameterException("Code index range from 100+");
		}
		else{
			String value = tfl.get(args[1]);
			if(value == null){
				throw new InvalidParameterException("Record not found.");
			}
			else{
				clntsock.send_byte(value);
			}
		}
	}
}
