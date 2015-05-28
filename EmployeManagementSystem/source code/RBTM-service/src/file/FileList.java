package file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import crypto.AESencrp;

public class FileList {

	private Map<String, String> list = new HashMap<String, String>();
	private String pwd = null;
	private String filePath = null;
	private boolean EXISTED = false;

	public FileList(String usrpasswd, String filePath) {
		this.pwd = usrpasswd;
		this.filePath = filePath;
		File file = new File(this.filePath);
		if (!file.exists()){
			EXISTED = false;
		} else {
		    Map<String, String> temp = FileOption.readFileToList(this.filePath);
	        if (temp != null){
	            decryptList(usrpasswd, temp, list);
	        }
		}
	}

	public void clear() {
	    this.list.clear();
	}

	public boolean isExisted() {
	    return EXISTED;
	}

	public ArrayList<String> getOwners() {
	    ArrayList<String> owners = new ArrayList<String>();
        try {
            Iterator<Map.Entry<String, String>> iter =  list.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                String key = entry.getKey();
                String val = entry.getValue();
                if (val.contains("o")) {
                    owners.add(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return owners;
	}

	public void saveFileList() {
		File file = new File(this.filePath);
		if (!file.exists()){
			try {
				if(!file.createNewFile()){
					System.out.println("failed to create "+filePath);
					return;
				} else {
				    EXISTED = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, String> temp = new HashMap<String, String>();
		encryptList(this.pwd, list, temp);
		FileOption.writeListToFile(temp, this.filePath);
		return;
	}

	public void encryptList(String usrpasswd, Map<String, String> from, Map<String, String> to){
		// encrypt user input...
		AESencrp aes = new AESencrp();
		aes.setALGO("AES");
		byte[] enckey = aes.getKey(usrpasswd);
		try {
			Iterator<Map.Entry<String, String>> iter = from.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<String, String> entry = iter.next();
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

	public void decryptList(String usrpasswd, Map<String, String> from, Map<String, String> to){
		// encrypt user input...
		AESencrp aes = new AESencrp();
		aes.setALGO("AES");
		byte[] enckey = aes.getKey(usrpasswd);
		try {
			Iterator<Map.Entry<String, String>> iter =  from.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<String, String> entry = iter.next();
			    String key = entry.getKey();
			    String val = entry.getValue();
			    key = aes.decrypt(key, enckey);
			    val = aes.decrypt(val, enckey);
			    to.put(key, val);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void add(String key, String value){
		list.put(key, value);
		list.put("Time", String.valueOf(new Date().getTime()));
		saveFileList();
	}

	public String get(String key){
		//System.out.println(list.get(key));
		return list.get(key);
	}

	public void printList(){
		if(list == null)
			return;
		try {
			Iterator<Map.Entry<String, String>> iter =  list.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<String, String> entry = iter.next();
			    String key = entry.getKey();
			    String val = entry.getValue();
			    System.out.print("<" + key + "," + val + ">");
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isOutdated() {
	    String time = get("Time");
	    if (time==null) { // not found time stamp
	        return true;
	    } else {
        	    long old = Long.valueOf(time).longValue();
        	    long current = new Date().getTime();
        	    if (current-old < 1000*60*60) {
        	        return true;
        	    } else {
        	    		return false;
        	    }
	    }
	}

	@Override
    public String toString() {
	    String s = "";

	    if(list == null)
            return s;
        try {
            Iterator<Map.Entry<String, String>> iter =  list.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                String key = entry.getKey();
                String val = entry.getValue();
                s += key+","+val+";";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

	    return s;
	}
}
