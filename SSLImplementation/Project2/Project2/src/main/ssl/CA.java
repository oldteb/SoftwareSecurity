package main.ssl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import main.crypto.RSAencrp;
import main.file.FileOption;

public class CA {

	static Map<String, Object> kmap = null;
	
	public static void CA_init(){

		File file = new File("CA_key.txt");
		if (file.exists() == false){
			try {
				if(file.createNewFile() == false){
					System.out.println("Data file creation failed.");
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		kmap = FileOption.readFileToKey("CA_key.txt");
	}
	
	public static String getPublicKey() throws Exception{
		return SimpleSSL.getPublicKey(kmap);
	}

	public static byte[] getCertificate(String publicKey, String id) throws Exception{
		String cer = id + publicKey;
		//System.out.println("1:"+publicKey.getBytes().length);
		byte[] sign = RSAencrp.sign(cer.getBytes(), SimpleSSL.getPrivateKey(kmap));
		//System.out.println(sign.length);
		byte[] certificate = SimpleSSL.byteCon(cer.getBytes(), sign);
		//System.out.println(cer.getBytes().length);

		return certificate;
	}

}
