package prolog;

import exception.OperationUnauthorizedException;

public class PrologQuery {
	
	int num_query = 0;
	private String id = null;
	private String[] temp_query = null;

	public PrologQuery(String id, String msg) throws OperationUnauthorizedException{

		this.id = id;
		temp_query = msg.split("\\$SEP\\$");
		temp_query[0] = temp_query[0] + "(" + id + ").";	
		
		
	}
	
	
	public String getSubQueryByIndex(int i){
		return temp_query[i];
	}
	
	public int getQueryNum(){
		return temp_query.length;
	}
	
}
