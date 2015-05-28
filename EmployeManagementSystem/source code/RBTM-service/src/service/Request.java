package service;

import java.io.IOException;

import exception.InvalidParameterException;

public interface Request {
	
	public void act() throws InvalidParameterException, IOException;
	
	
}
