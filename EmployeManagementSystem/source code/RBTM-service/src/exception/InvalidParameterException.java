package exception;

public class InvalidParameterException extends Exception{
	
    /** *//**
     * serializable
     */
    private static final long serialVersionUID = 2L;

    public InvalidParameterException(String msg){
        super(msg);
    }
	
}
