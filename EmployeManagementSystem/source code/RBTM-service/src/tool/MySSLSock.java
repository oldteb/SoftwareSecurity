package tool;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.security.cert.X509Certificate;

import crypto.RSAencrp;

public class MySSLSock {
	BufferedReader in = null;
	PrintWriter out = null;
	DataOutputStream out_byte = null;
	DataInputStream in_byte = null;
	SSLSocket sslsocket = null;
	
	public MySSLSock(SSLSocket ss){
		try {
			this.sslsocket = ss;
			this.out = new PrintWriter(ss.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(ss.getInputStream()));
			this.out_byte = new DataOutputStream(ss.getOutputStream());
			this.in_byte = new DataInputStream(ss.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int send(String output) {
		try{
			this.out.println(output);
			this.out.flush();
		}
		catch(Exception e){
			System.out.println("Send failed.");
			return -1;
		}
		return 0;
	}
	
	public void send_byte(String output_s) throws IOException {
		byte[] output = output_s.getBytes();
		out_byte.writeInt(output.length);
		out_byte.write(output);
		//out_byte.flush();
	}

	public String recv(){
		String input = null;
		try {
			input = this.in.readLine();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("close.");
			input = null;
		}       
		return input;
	}
	
	public String recv_byte() throws IOException {

		byte[] message = null;
	    int len = in_byte.readInt();
	    if(len > 0) {
	    	message = new byte[len];
	    	in_byte.readFully(message, 0, message.length); // read the message
	    }
	    return new String(message);
	}

	public void close() {
    	try {
    		this.out.close();
    		this.in.close();
    		this.sslsocket.close();
    	} catch (Exception e) {
    		return;
    	}
	} 
	
	public String getPeer() throws Exception {
	    SSLSession session = sslsocket.getSession();
        X509Certificate[] servercerts = session.getPeerCertificateChain();
        String dn = servercerts[0].getIssuerDN().getName();
        return MySSLSRVSock.getPeerFromDN(dn);
	}

	
	
}
