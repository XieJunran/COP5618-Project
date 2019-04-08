package cop5618.utility;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cop5618.utility.Tank.Direction;

public class Server {
	
	private static final int port = 5618;
	private static ArrayList<BattleField> bfs = new ArrayList<BattleField>();
	
	public static void main (String[] args) throws Exception {
		
		System.out.println("The server is running.");
		
        ServerSocket listener = new ServerSocket(port);
        int playerCounter = 0; // This indicates how many players once joined the game and  will not decrease even if a game has finished!!!
        
		try {
			while(true) {
				Socket connection = listener.accept();
				if (playerCounter++ % 4 == 0) {
					bfs.add(new BattleField());
					new Thread(bfs.get(bfs.size() - 1)).start();
				}
				new Handler(connection, bfs.get(bfs.size() - 1)).start();
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
      	
      	// HandShake process
      	private void handShake() throws IOException {
      		
      		System.out.println("Receive handshake message!");
      		rcv_msg = new byte[10];
      		in.read(rcv_msg);
      		String handShakeHeadString = new String(rcv_msg);
      		if (handShakeHeadString.equals("BATTLECITY")) {
      			int msg_length = in.readInt();;
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
      	
	}
  	
}
