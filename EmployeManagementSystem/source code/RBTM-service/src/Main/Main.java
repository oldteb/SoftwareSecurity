package Main;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import file.FileList;

public class Main {
	
	public static FileList fl = null;

	public static void main(String[] args) throws Exception {

		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		Console console_pass = System.console();
		// New a thread to run the server
		MyServer server = null;
		int port = 8989;
		try {
			//System.out.println("Please input the port for listening.");
			//port = Integer.parseInt(console.readLine());
			System.out.println("Please input the master password.");
			char[] pass = null;
			String passwd = "";
			if (console_pass != null &&
			    (pass = console_pass.readPassword()) != null) {
				passwd = new String(pass);
			    java.util.Arrays.fill(pass, ' ');
			}
			server = new MyServer(port,passwd);
			fl = new FileList(passwd, "datafile.txt");
		} catch (Exception e) {
			System.out.println("Server failed to start. Program ended.");
			System.exit(0);
		}
		server.start();

		while (true) {
			System.out.println("1.shutdown server and exit.");

			String option = console.readLine();
			if ("1".equals(option)) {
				server.interrupt();
				break;
			} else {
				System.out.println("Unknow Option.");
			}
		}
		
		// wait...
		server.join();
		
		System.out.println("Program Ended.");
	}
}
