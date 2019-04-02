package cop5618.utility;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	Socket requestSocket;           //socket connect to the server
	DataOutputStream out;           //stream write to the socket
	private final String hostName;
	private final int port;
	BattleField bf;
	byte[] snd_msg;                 //message send to the server
	

	public Client(String hostName, int port, BattleField bf) {
		
		this.hostName = hostName;
		this.port = port;
		this.bf = bf;
	}

	public void run() {
		
		try {
			//create a socket to connect to the server
			requestSocket = new Socket(hostName, port);
			
			//initialize inputStream and outputStream
			out = new DataOutputStream(requestSocket.getOutputStream());
			out.flush();
			
			// Send handshake and bitfield message
			handshake();
			sendMessage();
			
			while(bf.running) {
				
				// TODO
				
			}
			
		} catch (ConnectException e) {
    		System.err.println("Connection refused. You need to initiate a server first.");
		} catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		} catch(IOException ioException){
			ioException.printStackTrace();
		} catch(InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		} finally {
			//Close connections
			try{
				out.close();
				requestSocket.close();
				System.out.println("Client to [" + "] terminated!");
			} catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	
	public void sendMessage() throws IOException {
		
  		if (snd_msg != null) {
  			out.write(snd_msg);
  	  		out.flush();
  		}
  	}
  	
  	private void handshake() {
  		System.out.println("Client send [handshake] message");
  		snd_msg = new byte[10];
  		String str = "BATTLECITY";
  		snd_msg = str.getBytes();
  	}

}
