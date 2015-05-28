package service;
import java.io.IOException;

import tool.MySSLSock;
import exception.InvalidParameterException;
import file.FileList;
import Main.Main;

public class ReadDocRqst implements Request{
	
	String[] args = null;
	FileList tfl = null;
	MySSLSock clntsock = null;
	
	ReadDocRqst(MySSLSock socket, String[] args){
		this.args = args;
		tfl = Main.fl;
		this.clntsock = socket;
	}

	@Override
	public void act() throws InvalidParameterException, IOException{
		if(args.length != 2){
			throw new InvalidParameterException("expecting 1 arg, but recving " + (args.length-1));
		}
		else if(Integer.parseInt(args[1]) > 99){
			throw new InvalidParameterException("Doc index range from 0 ~ 99");
		}
		else{
			String value = tfl.get(args[1]);
			if(value == null){
				throw new InvalidParameterException("No record found.");
			}
			else
				clntsock.send_byte(value);
		}

		return;
	}
}
