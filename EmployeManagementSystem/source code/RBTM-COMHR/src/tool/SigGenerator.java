package tool;

import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import crypto.RSAencrp;
import tool.MySSLSRVSock;

public class SigGenerator {
	
	MySSLSRVSock srv_socket = null;
	
	public SigGenerator(MySSLSRVSock srv_socket){
		this.srv_socket = srv_socket;
	}
	
	public Boolean dotest(){
		String teststr = "This is a plaintext.";
		try {
			
			String cipher = encryptByPrivateKey(getHash(teststr));
			String s = decryptByPublicKey(cipher,"service");
			if(s.equals(getHash(teststr))){
				System.out.println("Yes!");
			}
			else
				System.out.println("No!");
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
		
    public String encryptByPrivateKey(String plaintext) throws Exception{
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // Encrypt the data
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, srv_socket.getPrivateKey());

        byte[] rst = cipher.doFinal(plaintext.getBytes());
        return RSAencrp.encryptBASE64(rst);
    }
    
    public String decryptByPublicKey(String ciphertext, String peerid) throws Exception{
    	byte[] cipherBytes = RSAencrp.decryptBASE64(ciphertext);
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // Decrypt the data
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, srv_socket.getPublicKeyByAlias(peerid));

        byte[] rst = cipher.doFinal(cipherBytes);
        return new String(rst);
    }
    
    public String getHash(String msg) throws Exception{
            MessageDigest md = MessageDigest.getInstance("MD5");
            // return the byte which is generated from the MD5 hash...
            return RSAencrp.encryptBASE64(md.digest(msg.getBytes()));
    }
	
}
