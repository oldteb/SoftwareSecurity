package Main;

import java.io.IOException;

import exception.OperationUnauthorizedException;
import exception.PrologExecutionException;
import prolog.PrologEngine;
import prolog.PrologQuery;
import service.CertGen;
import service.CertHelper;
import tool.MySSLSRVSock;
import tool.MySSLSock;


public class MyServer extends Thread{
	MySSLSRVSock srv_socket = null;
	MySSLSock clntsock = null;
	int port;
	boolean stop = false;

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
				//e.printStackTrace();
				System.out.println("Closed by server.");
				break;
			}
	    	System.out.println("New connection started.");

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
			    		System.out.println("Receive request from " + clntsock.getPeer());

			    		CertGen cg = new CertGen(clntsock.getPeer(), srv_socket);
			    		String certs = cg.getCerts(msg);
			    			
			    		if(certs == null){
			    			clntsock.send_byte("NONECER");
			    		}
			    		else{
			    			//System.out.println(certs);
			    			clntsock.send_byte(certs);
			    			
			    		}
			    	}
	    		}
		    	catch (PrologExecutionException pee){
		    		System.out.println(pee.getMessage());
		    		try {
						clntsock.send_byte("Invalid Command!");
					} catch (Exception e) {
						//e.printStackTrace();
						System.out.println("System Error: Msg send/recv failed!");
					}
		    	}
	    		catch (IOException ioe){
	    			System.out.println("System Error: Msg send/recv failed!");
	    			break;
	    		}
	    		catch (Exception e){
	    			System.out.println("System Error: Unkown reason!");
	    		}
	    	}//inner loop ends.
	    	
    		System.out.println("Connecttion closed.");
    		clntsock.close();
    		
    	}// outer loop ends.
    	
    	System.out.println("server thread closed.");
    }
	
}
