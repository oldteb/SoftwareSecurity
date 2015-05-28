package main.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MySocket {

	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	OutputStream out_byte = null;
	InputStream in_byte = null;
	String destination = null;

	public MySocket(Socket socket) {
		this.socket = socket;
		try {
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.out_byte = socket.getOutputStream();
			this.in_byte = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String output) {
		this.out.println(output);
		this.out.flush();
	}

	public void send(byte[] out) {
		int lenght = out.length;
		try {
			out_byte.write(out, 0, lenght);
			out_byte.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] receive_byte() {

		byte[] message = null;
	    try {
	    	int bufferSize = this.socket.getReceiveBufferSize();
	    	byte[] tmp = new byte [bufferSize];
			int len = this.in_byte.read(tmp);
			message = new byte[len];
			System.arraycopy(tmp, 0, message, 0, len);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return message;
	}


	public String receive() {
		String input = null;
		try {
			input = this.in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}

	public void close() {
		this.out.close();
    	try {
    		this.in.close();
    		this.socket.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	}
}
