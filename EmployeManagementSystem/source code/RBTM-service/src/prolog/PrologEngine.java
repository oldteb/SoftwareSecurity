package prolog;

import java.util.List;

import exception.PrologExecutionException;
import jpl.PrologException;
import jpl.Query;

public class PrologEngine {
	
	private static PrologEngine instance = null;
	
	private PrologEngine() throws PrologExecutionException{
		String temp = "consult('test2.pro')";
		Query q1 = new Query(temp);
		if(q1.hasSolution() == false){
			throw new PrologExecutionException("Prolog engine setup failed.");
		}
	}
	
	public static PrologEngine getInstance() throws PrologExecutionException{
		if(instance == null){
			instance = new PrologEngine();
		}
		return instance;
	}
	
	public Boolean execute(PrologQuery pq) throws PrologExecutionException{
		Boolean rst = false;
		for(int i = 1; i < pq.getQueryNum(); i++){
			runonce(toAssert(pq.getSubQueryByIndex(i)));
		}
		rst = runonce(pq.getSubQueryByIndex(0));
		for(int i = 1; i < pq.getQueryNum(); i++){
			runonce(toRetract(pq.getSubQueryByIndex(i)));
		}
		return rst;
	}
	
	private Boolean runonce(String sq) throws PrologExecutionException{
		Boolean rst = false;
		try{
			Query q1 = new Query(sq);
			rst = q1.hasSolution();
		}
		catch (PrologException pe){
			System.out.println("Engineer Error: when execute " + sq);
			throw new PrologExecutionException("Syntax Error.");
		}
		
		return rst;
	}
	
	private String toAssert(String s){
		String temp = "assert(" + s + ")";
		return temp;
	}
	
	private String toRetract(String s){
		String temp = "retract(" + s + ")";
		return temp;
	}
		
}
