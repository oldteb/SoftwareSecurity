package main.client;

import java.io.BufferedReader;
import java.net.InetAddress;
import java.net.Socket;

import main.ssl.SimpleSSL;
import main.tool.MySocket;

public class TLSClient {

	MySocket socket = null;
	BufferedReader console = null;

	public TLSClient(String address, String port, BufferedReader console) throws Exception {
		this.console = console;
		this.socket = new MySocket(new Socket(InetAddress.getByName(address), Integer.parseInt(port)));
	}

	public void run() {
		System.out.println("Client started.");

		// Handshake
		String server_address = null;
		try {
			this.socket.send(SimpleSSL.handShankMsg_send());
			byte[] input = this.socket.receive_byte();
			String result = SimpleSSL.handShankMsg_recv(input);
			if (result!=null) {
				server_address = result;
			} else {
				System.out.println("Handshake failed.");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				String input = console.readLine();
				// destroy the connection
				if ("exit".equals(input)) {
					this.socket.send(SimpleSSL.encrypt("DESTROY_CONNECTION", server_address));
					break;
				// send the typed massage to server and display returned value
				} else {
					this.socket.send(SimpleSSL.encrypt(input, server_address));
					String msg = SimpleSSL.decrypt(this.socket.receive_byte(), server_address);
					System.out.println("returned: "+msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
