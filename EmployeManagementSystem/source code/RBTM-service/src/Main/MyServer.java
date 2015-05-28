package Main;

import java.io.IOException;

import author.Authorizer;
import exception.InvalidParameterException;
import exception.PrologExecutionException;
import prolog.PrologEngine;
import prolog.PrologQuery;
import service.Request;
import service.RequestHandlerFactory;
import tool.MySSLSRVSock;
import tool.MySSLSock;


public class MyServer extends Thread{
	MySSLSRVSock srv_socket = null;
	MySSLSock clntsock = null;
	int port;
	boolean stop = false;
	RequestHandlerFactory rhf = null;

	public MyServer(int port, String pass) throws Exception {
		this.port = port;
		srv_socket = new MySSLSRVSock(port,pass);
	}

	@Override
	public void interrupt() {
		stop = true;
		if(clntsock != null){
			clntsock.close();
		}
		srv_socket.close();
	}
	
	@Override
    public void run() {
    	System.out.println("Server started.");
    	String msg = null;

    	while (stop == false) {
    		clntsock = null;
	    	try {
	    		clntsock = srv_socket.accept();
			} catch (Exception e) {
				System.out.println("Closed by server.");
				break;
			}
	    	System.out.println("New connection started.");
	    	rhf = new RequestHandlerFactory(clntsock);

	    	while(stop == false){
	    		try{
	    			msg = clntsock.recv_byte();
	    			if(msg == null){
	    				break;
	    			}
	    			else if(msg.equals("CLOSE_CONNECTION")){
	    				break;
	    			}
	    			else{
	    				//System.out.println(msg);
	    				System.out.println("Receive request from " + clntsock.getPeer());
		    			Authorizer auth = new Authorizer(srv_socket);
		    			if(auth.deal(clntsock.getPeer(), msg) == false){
		    				clntsock.send_byte("Unauthorized Operation!");
		    				continue;
		    			}
						else{
							System.out.println("authorized.");
							Request rqst = rhf.createHandler(msg);
							if(rqst == null){
								clntsock.send("Invalid cmd.");
							}
							else{
								rqst.act();
							}
							
						}
	    			}
	    		}
		    	catch (PrologExecutionException pee){
		    		System.out.println(pee.getMessage());
		    		try {
						clntsock.send_byte("Operation falied!");
					} catch (Exception e) {
						//e.printStackTrace();
						System.out.println("System Error: Msg send/recv failed!");
					}
		    	}
	    		catch (InvalidParameterException ipe){
	    			try {
						clntsock.send_byte("Invalid Parameter: " + ipe.getMessage());
					} catch (IOException e) {
						e.printStackTrace();
					}
	    		}
	    		catch (IOException ioe){
	    			System.out.println("System Error: Msg send/recv failed!");
	    			break;
	    		}
	    		catch (Exception e){
	    			System.out.println("System Error: Unkown reason!");
	    			break;
	    		}
	    	}//inner loop ends.
	    	
			System.out.println("Connecttion closed.");
			clntsock.close();
			
    	}//outer loop ends.
    	
    	System.out.println("server thread closed.");
    }
	
}
