package me.java.passwallet.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.java.passwallet.model.Record;

/**
 * 
 * @author diwang
 *
 */
public class FileOption {

	public static void writeListToFile(List<Record> recordList, String path) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(recordList);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Record> readFileToList(String path) {
		FileInputStream fis;
		List<Record> recordList = new ArrayList<Record>();
		try {
			fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			recordList = (List<Record>) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recordList;
	}

}
