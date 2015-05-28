package main.ssl;

import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import main.crypto.RSAencrp;

public class SimpleSSL {

	static String id = null;
	static byte[] Certificate  = null;
	static int CA_len = 0;
	static final int sig_len = 128;
	static int key_len = 0;
	static Map<String,Object> map = null;

	static Map<String,String> PKMgr = null;


	public static void init(String id) throws Exception{
		SimpleSSL.id = id;
		CA.CA_init();
		// generate key pair
		map = KeyGenerator.genKeyPair();
		SimpleSSL.key_len = getPublicKey(map).length();

		SimpleSSL.Certificate = CA.getCertificate(getPublicKey(map), id);
		SimpleSSL.CA_len = SimpleSSL.Certificate.length;

		SimpleSSL.PKMgr = new HashMap<String,String>();

	}

	 public static String getPublicKey(Map<String, Object> keyMap)throws Exception{
	        Key key = (Key) keyMap.get("PUBLIC_KEY");
	        return RSAencrp.encryptBASE64(key.getEncoded());
	 }

	 public static String getPrivateKey(Map<String, Object> keyMap)throws Exception{
	        Key key = (Key) keyMap.get("PRIVATE_KEY");
	        return RSAencrp.encryptBASE64(key.getEncoded());
	 }



	public static byte[] handShankMsg_send() throws Exception{
		byte[] HSByte = null;

		byte[] sig = RSAencrp.sign(id.getBytes(),SimpleSSL.getPrivateKey(map));
        byte[] hsMsg1 = byteCon(id.getBytes(), sig);

        HSByte = byteCon(hsMsg1, Certificate);

		return HSByte;
	}

	public static String handShankMsg_recv(byte[] HSByte) throws Exception{

		// verify CA's signature
        byte[] cipherText = new byte[HSByte.length-SimpleSSL.CA_len];
        byte[] certificate = new byte[SimpleSSL.CA_len];
        System.arraycopy(HSByte, 0, cipherText, 0, HSByte.length-SimpleSSL.CA_len);
        System.arraycopy(HSByte, HSByte.length-SimpleSSL.CA_len, certificate, 0, SimpleSSL.CA_len);
        // get cer and hash
        byte[] cer = new byte[certificate.length-sig_len];
        byte[] sign_ca = new byte[sig_len];
        System.arraycopy(certificate, 0, cer, 0, certificate.length-sig_len);
        System.arraycopy(certificate, certificate.length-sig_len, sign_ca, 0, sig_len);

        if (RSAencrp.verify(cer, CA.getPublicKey(), sign_ca) == false){
			System.out.println("CA_Signature error.");
			return null;
        }

        //String id = new String(cer);
        //id = id.substring(0, 1);

        byte[] pk = new byte[SimpleSSL.key_len];
        byte[] bid = new byte[cer.length-SimpleSSL.key_len];
        System.arraycopy(cer, cer.length-SimpleSSL.key_len, pk, 0, SimpleSSL.key_len);
        System.arraycopy(cer, 0, bid, 0, cer.length-SimpleSSL.key_len);
        String spk = new String(pk);

        byte[] sign = new byte[sig_len];
        byte[] text = new byte[cipherText.length-sig_len];
        System.arraycopy(cipherText, 0, text, 0, cipherText.length-sig_len);
        System.arraycopy(cipherText, cipherText.length-sig_len, sign, 0, sig_len);
        if (RSAencrp.verify(text, spk, sign) == false){
			System.out.println("Signature error.");
			return null;
        }

        // add into public key manager
        PKMgr.put(new String(bid),spk);

        //System.out.println(PKMgr.get("A"));

		return new String(bid);
	}

	public static byte[] encrypt(String plaintext, String dest) {
		byte[] enctext = null;
		// retrieve dest's public key by dest

		byte[] sig = null;
		try {
			sig = RSAencrp.sign(plaintext.getBytes(),SimpleSSL.getPrivateKey(map));
			enctext = RSAencrp.encryptByPublicKey(plaintext.getBytes(), PKMgr.get(dest));
		} catch (Exception e) {
			e.printStackTrace();
		}

        byte[] cipherByte = byteCon(enctext, sig);
        //String cipherText = RSAencrp.encryptBASE64(cipherByte);

		return cipherByte;
	}

	public static String decrypt(byte[] cipherByte, String source) throws Exception{
		byte[] plainText = null;

		// retrieve dest's public key by source
		RSAPublicKey publicKey = null;

		//byte[] cipherByte = RSAencrp.decryptBASE64(enctext);

        byte[] cipherText = new byte[cipherByte.length-128];
        byte[] sign = new byte[128];
        System.arraycopy(cipherByte, 0, cipherText, 0, cipherByte.length-128);
        System.arraycopy(cipherByte, cipherByte.length-128, sign, 0, 128);

        plainText = RSAencrp.decryptByPrivateKey(cipherText, SimpleSSL.getPrivateKey(map));

		if(RSAencrp.verify(plainText,PKMgr.get(source),sign) == false){
			System.out.println("Signature error.");
			return null;
		}

		return new String(plainText);
	}


	public static byte[] byteCon(byte[] s1, byte[] s2){
        byte[] tar = new byte[s1.length+s2.length];
        System.arraycopy(s1, 0, tar, 0, s1.length);
        System.arraycopy(s2, 0, tar, s1.length, s2.length);

        return tar;
	}

}
