package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import main.client.TLSClient;
import main.crypto.AESencrp;
import main.file.FileList;
import main.server.TLSServer;
import main.ssl.SimpleSSL;

public class Main {

	public static void main(String[] args) throws Exception {

		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

		// Login to the system
		File tmp = new File("tmp.txt");
		boolean isPwdExisted = tmp.exists();
		AESencrp encrypt = new AESencrp();
		if (!isPwdExisted) { // the first time to start the program
			System.out.println("Please type a new password.");
		} else {
			System.out.println("Please type password.");
		}
		String pwd = console.readLine();
		byte[] hash = encrypt.getHashForPwd(pwd);

		if (!tmp.exists()) {
			FileOutputStream fos = new FileOutputStream(tmp);
			fos.write(hash);  // save hash of password to local file
			fos.close();
		} else {
			FileInputStream fis = new FileInputStream(tmp);
			byte[] temp = new byte[1000];
			int len = fis.read(temp);  // read hash of password from local file
			byte[] value = new byte[len];
			System.arraycopy(temp, 0, value, 0, len);
			fis.close();
			if (!Arrays.equals(hash, value)) {  // hash doesn't match
				System.out.println("Wrong Password. Program Ended.");
				System.exit(0);
			}
		}
		System.out.println("Login Successfully.");

		// Initialize the data from existed file
		FileList fileList = new FileList();
		fileList.Init(pwd);
		fileList.printList();

		// New a thread to run the server
		System.out.println("Please input the port for listening.");
		int port = 0;
		TLSServer server = null;
		try {
			port = Integer.parseInt(console.readLine());
			server = new TLSServer(port, fileList);
		} catch (Exception e) {
			System.out.println("Server failed to start. Program ended.");
			System.exit(0);
		}
		server.start();

		// Initialize the SSL tool
		SimpleSSL.init("127.0.0.1:"+port);

		while (true) {
			System.out.println("1.start a client\n2.shutdown server and exit.");

			String option = console.readLine();
			if ("1".equals(option)) { // establish a connection with the below address
				System.out.println("Please input the address for connection.");
				String address = console.readLine();
				System.out.println("Please input the port for connection.");
				String port_for_client = console.readLine();
				try {
					TLSClient client = new TLSClient(address, port_for_client, console);
					client.run();
				} catch (Exception e) {
					System.out.println("Connection refused.");
				}
			} else if ("2".equals(option)) {  // end the whole program
				break;
			} else {
				System.out.println("Unknow Option.");
			}
		}

		// Save new data to local file
		fileList.saveFileList(pwd);

		server.interrupt();
		System.out.println("Program Ended.");
		System.exit(0);
	}
}
