package prolog;

import java.util.Hashtable;
import java.util.List;

import exception.PrologExecutionException;
import jpl.Atom;
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
		String[] ret = new String[3];
		for(int i = 1; i < pq.getQueryNum(); i++){
			runonce(toAssert(pq.getSubQueryByIndex(i)));
		}
		if(pq.ret_val > 0){
			if(runonce(pq.getSubQueryByIndex(0), ret)){
				pq.result = ret[0];
				rst = true;
			}
		}
		else{
			if(runonce(pq.getSubQueryByIndex(0))){
				pq.result = "true";
				rst = true;
			}
		}
		for(int i = 1; i < pq.getQueryNum(); i++){
			runonce(toRetract(pq.getSubQueryByIndex(i)));
		}
		
		return rst;
	}

	
	private Boolean runonce(String sq) throws PrologExecutionException{
		Boolean rst = null;
		try{
			Query q1 = new Query(sq);
			rst = q1.hasSolution();
		}
		catch (PrologException pe){
			throw new PrologExecutionException("Syntax Error.");
		}
		
		return rst;
	}
	
	private Boolean runonce(String sq, String[] strs) throws PrologExecutionException{
		Boolean rst = null;
		try{
			String trst = "";
			Query q1 = new Query(sq);
			if(q1.hasSolution() == true)
			{
				trst = q1.oneSolution().get("X").toString();
			}
			else{
				return false;
			}
			String[] part = sq.split(",");
			strs[0] = part[0] + "," + trst + ")";
			if(strs[0] == null){
				rst = false;
			}
			else{
				rst = true;
			}
		}
		catch (PrologException pe){
			throw new PrologExecutionException("Syntax Error.");
		}
		
		return rst;
	}
	
	private String toAssert(String s){
		String temp = "assert(" + s + ").";
		return temp;
	}
	
	private String toRetract(String s){
		String temp = "retract(" + s + ").";
		return temp;
	}
		
}
