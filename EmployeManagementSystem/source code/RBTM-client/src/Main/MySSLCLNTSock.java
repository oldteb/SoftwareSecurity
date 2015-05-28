package Main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import propertie.ClientPropertie;

public class MySSLCLNTSock {
	
	private KeyStore ks = null;
	
	public MySSLCLNTSock(){
		try{
			ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ClientPropertie.getInstance().KeyFile), null);
		}
		catch(Exception e){ 
			e.printStackTrace();
		}
	}
	
	public Boolean loginVerify(String usrname, String pass){
		Boolean rst = false;
		try {
			Key key = ks.getKey(usrname, pass.toCharArray());
			if(key instanceof PrivateKey)
				rst = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rst;
	}
	
	public MySSLSock connect(String addr, String port, String pass) {
		MySSLSock socket = null;
		try{
			System.setProperty("javax.net.ssl.trustStore", ClientPropertie.getInstance().KeyFile);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, pass.toCharArray());
			SSLContext sc = SSLContext.getInstance("TLS"); 	 
			sc.init(kmf.getKeyManagers(), null, null); 
	        SocketFactory factory = sc.getSocketFactory();
	        socket = new MySSLSock((SSLSocket)factory.createSocket(addr, Integer.parseInt(port)));
		}
		catch(Exception e){ 
			e.printStackTrace();
		}
		return socket;
	}
}
