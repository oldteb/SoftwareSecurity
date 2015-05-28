package me.java.passwallet.crypto;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class AESencrp {

	private String ALGO;

	public String encrypt(String Data, byte[] keyValue) throws Exception {
		Key key = generateKey(keyValue);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}

	public String decrypt(String encryptedData, byte[] keyValue) throws Exception {
		Key key = generateKey(keyValue);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}
	
	public byte[] getKey(String rowkey){
		// standards key...
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // return the byte which is generated from the MD5 hash...
            return md.digest(rowkey.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
		return null;
	}

	// Generate the key...
	private Key generateKey(byte[] keyValue) throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}

	public String getALGO() {
		return ALGO;
	}

	//  Set enc/dec algorithm...
	public void setALGO(String aLGO) {
		ALGO = aLGO;
	}

}