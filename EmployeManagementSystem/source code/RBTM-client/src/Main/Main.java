package Main;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import Main.MySSLCLNTSock;

public class Main {
	
	public static void main(String argv[]){
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		Console console_pass = System.console();
		MySSLSock clnt_socket = null;
		MySSLCLNTSock ssl_sock = null;
		String passwd = null;
//		try {
//			System.out.println("Please input the address for connection.");
//			String clntaddr = console.readLine();
//			System.out.println("Please input the port for connection.");
//			String clntport = console.readLine();
//			System.out.println("Please input master password.");
//			String pass = console.readLine();
//			clnt_socket = MySSLCLNTSock.connect(clntaddr,clntport,pass);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		// Check if the user's identity match provided keystore.
		// If not match, reject login.
		try{
			ssl_sock = new MySSLCLNTSock();
			System.out.println("Please input username:");
			String usrname = console.readLine();
			System.out.println("Please input password:");
//			String passwd = console.readLine();
			char[] pass = null;
			if (console_pass != null &&
			    (pass = console_pass.readPassword()) != null) {
				passwd = new String(pass);
			    java.util.Arrays.fill(pass, ' ');
			}
			if(ssl_sock.loginVerify(usrname, passwd)){
				System.out.println("Login success.");
			}
			else{
				System.out.println("Invalid identity.\nApplication exit.");
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}

			
		System.out.println("Client starts.");
//		while (true) {
//			try {
//				System.out.print(">>");
//				String input = console.readLine();
//				// destroy the connection
//				if ("exit".equals(input)) {
//					clnt_socket.send_byte("CLOSE_CONNECTION");
//					break;
//				} else {
//					clnt_socket.send_byte("writeCode$SEP$comEmployee(baker)$SEP$manager(baker)");
//					String recvmsg = clnt_socket.recv_byte();
//					
//					if(recvmsg == null)
//						break;
//					else
//						System.out.println("returned: "+ recvmsg);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				break;
//			}
//		}
		while (true) {
			try {
				System.out.print(">>");
				String input = console.readLine();
				// connect to DPAHR
				clnt_socket = ssl_sock.connect("127.0.0.1","8991",passwd);
				String msg = null;
				// destroy the connection
				if ("exit".equals(input)) {
					clnt_socket.send_byte("CLOSE_CONNECTION");
					break;
				} else {
					clnt_socket.send_byte("MSG1");
					String recvmsg = clnt_socket.recv_byte();
					msg = input + recvmsg;
				}
				clnt_socket.send_byte("CLOSE_CONNECTION");
				clnt_socket.close();
				
				// connect to ComHR
				clnt_socket = ssl_sock.connect("127.0.0.1","8990",passwd);
				clnt_socket.send_byte(msg);
				String recvmsg = clnt_socket.recv_byte();
				msg += recvmsg;
				clnt_socket.send_byte("CLOSE_CONNECTION");
				clnt_socket.close();
				
				// connect to service
				clnt_socket = ssl_sock.connect("127.0.0.1","8989",passwd);
				clnt_socket.send_byte(msg);
				recvmsg = clnt_socket.recv_byte();
				System.out.println("service retrure: " + recvmsg);
				clnt_socket.send_byte("CLOSE_CONNECTION");
				clnt_socket.close();
			} 
			catch (IOException ioe){
    			System.out.println("System Error: Msg send/recv failed!");
    			break;
			}
			catch (Exception e) {
				//e.printStackTrace();
				break;
			}
			
		}//loop ends...
		
		
		System.out.println("Client closed.");	
	}
	

}
