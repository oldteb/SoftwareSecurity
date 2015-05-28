package file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileOption {

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

	@SuppressWarnings("unchecked")
	public static Map<String, String> readFileToList (String path) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Map<String, String> list = new HashMap<String, String>();
		try {
			fis = new FileInputStream(path);
			ois = new ObjectInputStream(fis);
			list = (Map<String, String>) ois.readObject();
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
				ois.close();
			} catch (Exception e) {
			}

		}
		return list;
	}




}
