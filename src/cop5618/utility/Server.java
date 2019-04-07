package cop5618.utility;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cop5618.utility.Tank.Direction;

public class Server {
	
	private static final int port = 5618;
	
	public static void main (String[] args) throws Exception {
		
		System.out.println("The server is running.");
		
        ServerSocket listener = new ServerSocket(port);
        BattleField bf = new BattleField();
        int playerCounter = 1; // This indicates how many players once joined the game and  will not decrease even if a game has finished!!!
        
		try {
			while(true) {
				if (playerCounter % 4 == 0) bf = new BattleField();
				new Handler(listener.accept(), bf).start();
				++playerCounter;
			}
		} finally {
			listener.close();
			System.out.println("Server terminate successfully!");
		}
	}

	/**
   	 * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for dealing with a single client's requests.
     */
    private static class Handler extends Thread {
    	
    	private byte[] rcv_msg;    //message received from the client
    	
    	private final Socket connection;
    	private final BattleField bf;
    	private Tank tank;
    	
       	private DataInputStream in;	//stream read from the socket

       	public Handler(Socket connection, BattleField bf) {
       		this.connection = connection;
       		this.bf = bf;
       	}

        public void run() {
        	
        	try{
        		//initialize Input and Output streams
        		in = new DataInputStream(connection.getInputStream());
    			handShake(); // handshake
    			
       			while(!bf.isEnded()) {
        			
        			receiveMessage();  //receive the message sent from the client
        			
       				int msg_type = byteArrayToInt(rcv_msg);
       	      		
       	      		switch (msg_type) {
       	      		// 
       	      		case 0: {
       	      			// DO NOTHING
       	      		}
       	      		break;
       	      		// Move
       	      		case 1: {
       	      			tank.move(Direction.UP);
       	      		}
       	      		break;
       	      		// 
       	      		case 2: {
       	      			tank.move(Direction.DOWN);
       	      		}
       	      		break;
       	      		// 
       	      		case 3: {
       	      			tank.move(Direction.LEFT);
       	      		}
       	      		break;
       	      		// 
       	      		case 4: {
       	      			tank.move(Direction.RIGHT);
       	      		}
       	      		break;
       	      		// 
       	      		case 5: {
       	      			tank.move(Direction.UP);
       	      			tank.fire();
       	      		}
       	      		break;
       	      		// 
       	      		case 6: {
       	      			tank.move(Direction.DOWN);
       	      			tank.fire();
       	      		}
       	      		break;
       	      		// 
       	      		case 7: {
       	      			tank.move(Direction.LEFT);
       	      			tank.fire();
       	      		}
       	      		break;
       	      		// 
       	      		case 8: {
       	      			tank.move(Direction.RIGHT);
       	      			tank.fire();
       	      		}
       	      		break;
       	      		//
       	      		case 9: {
       	      			tank.fire();
       	      		}
       	      		break;
       	      		default: {
       	      			System.out.println("invalid message type!");
       	      		}
       	      		}
        		}
        	} catch(IOException ioException) {
        		ioException.printStackTrace();
			} finally {
        		//Close connections
        		try {
        			in.close();
        			connection.close();
        			System.out.println("Server Thread connecting to client [" + "] terminated");
				} catch(IOException ioException) {
					System.out.println("Disconnect with Client ");
				}
			}
        	
 		}
      	
      	private void receiveMessage() throws IOException {
      		
      		rcv_msg = new byte[4];
      		in.read(rcv_msg);
      	}
      	
      	// HandShake process
      	private void handShake() throws IOException {
      		
      		System.out.println("Receive handshake message!");
      		rcv_msg = new byte[10];
      		in.read(rcv_msg);
      		
      		String handShakeHeadString = new String(rcv_msg);
      		if (handShakeHeadString.equals("BATTLECITY")) {
      			rcv_msg = new byte[4];
      			in.read(rcv_msg);
      			int msg_length = byteArrayToInt(rcv_msg);
      			rcv_msg = new byte[msg_length];
      			in.read(rcv_msg);
      			String str = new String(rcv_msg);
      			String[] hostAndPort = str.split(" ");
      			String host = hostAndPort[0];
      			int port = Integer.parseInt(hostAndPort[1]);
      			Client client = new Client(host, port, bf);
      			tank = bf.AddTank(client);
      			client.start();
      			// TODO
      		}
      		else {
      			System.out.println("Handshake unsuccessful!");
      			// TODO
      		}
      	}
      	
      	// Convert byte array to int
      	public int byteArrayToInt(byte[] b) { 
      		
      	    return   b[3] & 0xFF |  
      	            (b[2] & 0xFF) << 8 |  
      	            (b[1] & 0xFF) << 16 |  
      	            (b[0] & 0xFF) << 24;  
      	}
      	
	}
  	
}
