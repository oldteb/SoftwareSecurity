package main.server;

import java.net.ServerSocket;

import main.file.FileList;
import main.ssl.SimpleSSL;
import main.tool.MySocket;

/*
 * The class will be run as a thread, the main thread runs as a client
 */
public class TLSServer extends Thread {

	MySocket socket = null;
	ServerSocket ss = null;
	FileList fl = null;
	int port;

	public TLSServer(int port, FileList fl) throws Exception {
		this.port = port;
		this.ss = new ServerSocket(this.port);
		this.fl = fl;
	}

	@Override
	public void interrupt() {
    	System.out.println("Server stopped.");
	}

	@Override
    public void run() {
    	System.out.println("Server started.");

    	while (true) {

    		// build a new listener
	    	try {
				this.socket = new MySocket(this.ss.accept());
			} catch (Exception e) {
				return;
			}

	    	// Handshake to Establish a connection
	    	byte[] hello = this.socket.receive_byte();
	    	String client_address = null;
	    	try {
	    		String result = SimpleSSL.handShankMsg_recv(hello);
				if (result!=null) {
					client_address = result;
					this.socket.send(SimpleSSL.handShankMsg_send());
				} else {
					System.out.println("failed.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

	    	// Process request from a client
			while (true) {

				// Decrypt byte[] to Message
	        	byte[] input = this.socket.receive_byte();
	        	String msg = null;
	        	try {
	        		msg = SimpleSSL.decrypt(input, client_address);
				} catch (Exception e) {
					e.printStackTrace();
				}

	        	if (msg==null || "DESTROY_CONNECTION".equals(msg)) {  // receive disconnect message
	        		this.socket.close();
	        		break;
	        	} else if (msg.contains(",")) { // receive command to store a new data
	        		String[] result = msg.split(",");
	        		try {
		        		fl.add(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
		        		this.socket.send(SimpleSSL.encrypt("Store Successfully.", client_address));
	        		} catch (NumberFormatException e) {
	        			this.socket.send(SimpleSSL.encrypt("Wrong Format.", client_address));
	        		} catch (Exception e1) {
	        			this.socket.send(SimpleSSL.encrypt("Store Failed.", client_address));
	        		}
	        	} else { // receive command to get a data
	        		try {
	        			int result = Integer.parseInt(msg);
	        			this.socket.send(SimpleSSL.encrypt(Integer.toString(fl.get(result)), client_address));
	        		} catch (NumberFormatException e) {
	        			this.socket.send(SimpleSSL.encrypt("Wrong Format.", client_address));
	        		} catch (Exception e1) {
	        			this.socket.send(SimpleSSL.encrypt("Not Found.", client_address));
	        		}
	        	}
	        }
    	}
    }
}
