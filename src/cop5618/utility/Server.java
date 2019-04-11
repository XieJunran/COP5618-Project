package cop5618.utility;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cop5618.utility.Tank.Direction;

public class Server {
	
	private static final int port = 5618;
	
	public static void main (String[] args) throws Exception {
		
		System.out.println("Server is running.");
		
        ServerSocket listener = new ServerSocket(port);
        BattleField bf = null;
        int playerCounter = 0; // This indicates how many players once joined the game and  will not decrease even if a game has finished!!!
        
		try {
			while(true) {
				Socket connection = listener.accept();
				if (playerCounter++ % 5 == 0 || bf.isEnded() != -1) {
					bf = new BattleField();
					new Thread(bf).start();
				}
				new Handler(connection, bf).start();
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
        		in = new DataInputStream(connection.getInputStream());
    			handShake();
    			
       			while(bf.isEnded() == -1) {
        			
       				int msg_type = in.readInt();
       	      		
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
       	      			System.err.println("Invalid message type!");
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
      	
      	// HandShake process
      	private void handShake() throws IOException {
      		
      		System.out.println("Receive handshake message!");
      		byte[] rcv_msg = new byte[10];
      		in.read(rcv_msg);
      		String handShakeHeadString = new String(rcv_msg);
      		if (handShakeHeadString.equals("BATTLECITY")) {
      			int msg_length = in.readInt();
      			rcv_msg = new byte[msg_length];
      			in.read(rcv_msg);
      			String host = new String(rcv_msg);
      			Client client = new Client(host, bf);
      			tank = bf.AddTank(client);
      			client.start();
      		}
      		else {
      			System.err.println("Handshake unsuccessful!");
      			// TODO
      		}
      	}
      	
	}
  	
}
