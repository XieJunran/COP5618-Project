package cop5618.utility;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	private Socket requestSocket;           //socket connect to the server
	private final int CLIENT_PORT = 5619;
	private DataOutputStream out;           //stream write to the socket
	private BattleField bf;
	private final String hostName;
	private int[][] field;
	private volatile boolean fieldUpdated = false;
	

	public Client(String hostName, BattleField bf) {
		
		this.hostName = hostName;
		this.bf = bf;
	}

	public void run() {
		
		try {
			//create a socket to connect to the server
			requestSocket = new Socket(hostName, CLIENT_PORT);
			
			//initialize inputStream and outputStream
			out = new DataOutputStream(requestSocket.getOutputStream());
			out.flush();
			
			// Send handshake and bitfield message
			handshake();
			
			while(!bf.isEnded()) {
				
				if (fieldUpdated) {
					for (int i = 0; i < BattleField.BF_SIZE; ++i) {
						for (int j = 0; j < BattleField.BF_SIZE; ++j) {
							out.writeInt(field[i][j]);
						}
					}
					out.flush();
					fieldUpdated = false;
				}
				
			}
			
		} catch (ConnectException e) {
    		System.err.println("Connection refused. You need to initiate a server first.");
		} catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		} catch(IOException ioException){
			ioException.printStackTrace();
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
  	
  	private void handshake() {
  		
  		System.out.println("Client send [handshake] message");
  		try {
  			out.write("BATTLECITY".getBytes());
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
  		
  	}
  	
  	public void sendBF(int[][] field) {
  		
  		this.field = field;
  		fieldUpdated = true;
  	}

}
