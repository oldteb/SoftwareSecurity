package main.ssl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

public class KeyGenerator {

	public static Map<String,Object> genKeyPair() throws NoSuchAlgorithmException{
		Map<String,Object> keyMap = new HashMap<String,Object>(2);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // public key
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // private key
        RSAPrivateKey privateKey =  (RSAPrivateKey) keyPair.getPrivate();

        keyMap.put("PUBLIC_KEY", publicKey);
        keyMap.put("PRIVATE_KEY", privateKey);

        return keyMap;
	}
}
