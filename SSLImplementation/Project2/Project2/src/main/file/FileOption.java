package main.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileOption {

	String usrInput = null;


	public static void writeListToFile(Map<String, String> list, String path) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> readFileToList (String path) {
		FileInputStream fis;
		Map<String, String> list = new HashMap<String, String>();
		try {
			fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			list = (Map<String, String>) ois.readObject();
		} catch (Exception e) {
		}
		return list;
	}
	
	public static void writeKeyToFile(Map<String, Object> list, String path) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Object> readFileToKey (String path) {
		FileInputStream fis;
		Map<String, Object> list = new HashMap<String, Object>();
		try {
			fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			list = (Map<String, Object>) ois.readObject();
		} catch (Exception e) {
		}
		return list;
	}




}
