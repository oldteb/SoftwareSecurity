package tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class MySSLSRVSock {

	SSLServerSocket srv_socket = null;
	private KeyStore ks = null;
	private Key key = null;

	public MySSLSRVSock(int port, String password) {
		try{
			System.setProperty("javax.net.ssl.trustStore", "DPAHR.key");
			// initialize a new SSLSocket
			ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream("DPAHR.key"), password.toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, password.toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509"); 
			tmf.init(ks);
			TrustManager[] trustManagers = tmf.getTrustManagers();

			SSLContext sc = SSLContext.getInstance("TLS"); 	 
			sc.init(kmf.getKeyManagers(), trustManagers, null); 

			SSLServerSocketFactory ssf = sc.getServerSocketFactory(); 
			srv_socket = (SSLServerSocket) ssf.createServerSocket(port);
			srv_socket.setNeedClientAuth(true);
			
			key = ks.getKey("DPAHR", password.toCharArray());
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public MySSLSock accept() throws Exception{
		MySSLSock clntsock = null;
		clntsock = new MySSLSock((SSLSocket)srv_socket.accept());

		return clntsock;
	}
	
	public void close(){
		try {
			srv_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static String getPeerFromDN(String dn) {
        String peer = null;
        Pattern p = Pattern.compile("CN=([A-Za-z]+),.*");
        Matcher m = p.matcher(dn);
        if (m.find()) {
            peer = m.group(1);
        } else {
            System.out.println("Peer Not Found.");
        }
        return peer;
    }
    
    public Key getPrivateKey(){
		return key;
    }
    
    public Key getPublicKeyByAlias(String name){
    	PublicKey publicKey = null;
        // Get certificate of public key
		try {
			Certificate cert = ks.getCertificate(name);
			publicKey = cert.getPublicKey();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return publicKey;
    }

}
