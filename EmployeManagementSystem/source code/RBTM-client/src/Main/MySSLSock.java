package Main;

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
			//this.out.println(output);
			//this.out.write(output, 0, output.length());
			out.write(output.toCharArray(), 0, output.toCharArray().length);
			this.out.flush();
		}
		catch(Exception e){
			System.out.println("Send failed.");
			return -1;
		}
		return 0;
	}
	
	public void send_byte(String output_s) throws Exception {
		byte[] output = output_s.getBytes();
		try {
			out_byte.writeInt(output.length);
			out_byte.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String recv(){
		String input = null;
		char[] cbuf = new char[65536];
		char[] tar = null;
		try {
			//input = this.in.readLine();
			int count = this.in.read(cbuf);
			tar = new char[count];
			System.arraycopy(cbuf, 0, tar, 0, count);
			input = new String(tar);
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("close.");
			input = null;
		}
		return input;
	}
	
	public String recv_byte() throws Exception {

		byte[] message = null;
	    try {
	    	int len = in_byte.readInt();
	    	if(len > 0) {
	    		message = new byte[len];
	    		in_byte.readFully(message, 0, message.length); // read the message
	    	}
	    	
		} catch (IOException e) {
			e.printStackTrace();
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
	
//	public String getPeer() throws Exception {
//	    SSLSession session = sslsocket.getSession();
//        X509Certificate[] servercerts = session.getPeerCertificateChain();
//        String dn = servercerts[0].getIssuerDN().getName();
//        return MySSLSRVSock.getPeerFromDN(dn);
//	}

	
	
}
