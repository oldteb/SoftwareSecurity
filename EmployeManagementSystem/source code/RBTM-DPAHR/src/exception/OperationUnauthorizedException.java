package exception;

public class OperationUnauthorizedException extends Exception{
	
    /** *//**
     * serializable
     */
    private static final long serialVersionUID = 1L;

    public OperationUnauthorizedException(String msg){
        super(msg);
    }
	
}
