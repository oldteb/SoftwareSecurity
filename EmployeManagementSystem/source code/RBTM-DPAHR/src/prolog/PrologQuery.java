package prolog;

import exception.OperationUnauthorizedException;
import exception.PrologExecutionException;

public class PrologQuery {
	
	public int ret_val = 0;
	private String id = null;
	private String[] temp_query = null;
	String result = "";

	public PrologQuery(String id, String msg) throws PrologExecutionException{
		this.id = id;
		temp_query = msg.split("\\$SEP\\$");
		System.out.println(temp_query[0]);
		temp_query[0] = temp_query[0] + "(" + id + ").";	
	}
	
	public PrologQuery(String id, String X, String msg) throws PrologExecutionException{
		ret_val = 1;
		this.id = id;
		if(!X.equals("Variable")){
			throw new PrologExecutionException("Syntax Error.");
		}
		temp_query = msg.split("\\$SEP\\$");
		temp_query[0] = temp_query[0] + "(" + id + ",X" + ").";
	}
	
	
	public String getSubQueryByIndex(int i){
		return temp_query[i];
	}
	
	public int getQueryNum(){
		return temp_query.length;
	}

	public String getResult(){
		return result;
	}
	
}
