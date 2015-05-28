package main.file;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import main.crypto.AESencrp;

public class FileList {

	Map<Integer, Integer> list = new HashMap<Integer, Integer>();

	public void Init(String usrpasswd) throws IOException {
		File file = new File("StoredData.txt");
		if (file.exists() == false){
			if(file.createNewFile() == false){
				System.out.println("Data file creation failed.");
				return;
			}
		}
		Map<String, String> temp = FileOption.readFileToList("StoredData.txt");
		if (temp != null){
			decryptList(usrpasswd, temp, list);
		}
	}

	public void saveFileList(String usrpasswd) throws IOException{
		File file = new File("StoredData.txt");
		if (file.exists() == false){
			if(file.createNewFile() == false){
				System.out.println("Data file creation failed.");
				return;
			}
		}
		Map<String, String> temp = new HashMap<String, String>();
		encryptList(usrpasswd, list, temp);
		FileOption.writeListToFile(temp, "StoredData.txt");
		return;
	}

	public void encryptList(String usrpasswd, Map<Integer, Integer> from, Map<String, String> to){
		// encrypt user input...
		AESencrp aes = new AESencrp();
		aes.setALGO("AES");
		byte[] enckey = aes.getKey(usrpasswd);
		try {
			Iterator iter = from.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry entry = (Map.Entry) iter.next();
			    String key = entry.getKey().toString();
			    String val = entry.getValue().toString();
			    key = aes.encrypt(key, enckey);
			    val = aes.encrypt(val, enckey);
			    to.put(key, val);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void decryptList(String usrpasswd, Map<String, String> from, Map<Integer, Integer> to){
		// encrypt user input...
		AESencrp aes = new AESencrp();
		aes.setALGO("AES");
		byte[] enckey = aes.getKey(usrpasswd);
		try {
			Iterator iter =  from.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry entry = (Map.Entry) iter.next();
			    String key = (String)entry.getKey();
			    String val = (String)entry.getValue();
			    key = aes.decrypt(key, enckey);
			    val = aes.decrypt(val, enckey);
			    to.put(new Integer(key), new Integer(val));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void add(int key, int value){
		list.put(key, value);
	}

	public int get(int key){
		return list.get(key);
	}

	public void printList(){
		if(list == null)
			return;
		try {
			Iterator iter =  list.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry entry = (Map.Entry) iter.next();
			    Integer key = (Integer)entry.getKey();
			    Integer val = (Integer)entry.getValue();
			    System.out.print("<" + key + "," + val + ">");
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
