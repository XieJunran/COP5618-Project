package cop5618;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import cop5618.utility.MultiPlayerGame;

@SuppressWarnings("serial")
public class MultiPlayerClient extends JFrame {
	
	private static final int port = 5618;
	
	private static final int scrw = 800;
	private static final int scrh = 800;
	
	private String serverip;
	
	private Socket socket;
	private ServerSocket listener;
	
	private JFrame play;
	private MultiPlayerGame game;
	
	public static void main(String[] args) {
		
		if (args.length != 1) {
			
			System.err.println("Please pass the server IP in command line argument!");
			
			return;
			
		}
		
		EventQueue.invokeLater(() -> {
			
            try {
            	
            	MultiPlayerClient frame = new MultiPlayerClient(args[0]);
                frame.setResizable(false);
                frame.setVisible(true);
                
            } catch (Exception e) {
            	
                e.printStackTrace();
                
            }
            
        });
		

	}
	
	class CheckLive implements Runnable {
		
		public void run() {
			
			while(game.isLive()) {
				
				try {
					
					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
			game.removeAll();
			play.remove(game);
			play.dispose();
			
			play = null;
			game = null;
			
			setVisible(true);
			
		}
		
	}
	
	
	private void StartGame () {
		
		play = new JFrame("Fights!");
		play.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		try {
			
			System.out.println("Creating Socket to " + serverip + " !");
			socket = new Socket(serverip, port);
			listener = new ServerSocket(port);
			
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.flush();
			DataInputStream in = new DataInputStream(listener.accept().getInputStream());
			
			System.out.println("HandShaking with the server!");
			InetAddress localhost = InetAddress.getLocalHost();
			String handShaking = "BATTLECITY " + localhost.getHostAddress().trim();
			
			System.out.println(handShaking);
			out.writeBytes(handShaking);
			out.flush();
			
			setVisible(false);
		
			game = new MultiPlayerGame(in, out);
		
			play.setContentPane(game);
			play.setBounds(game.getBounds());
			play.setVisible(true);
			play.setResizable(false);
		
			game.requestFocus();
			new Thread(new CheckLive()).start();
			
		} catch (IOException e) {
			
			System.err.println("Cannot connect to " + serverip + " !");
			e.printStackTrace();
			
		} finally {
			
			
			
		}
		
	}
	
	private MultiPlayerClient(String server) {
		
		serverip = server;
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(0, 0, scrw, scrh);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.WHITE);
		panel.setBackground(Color.BLACK);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btn = new JButton("start!");
		btn.setBounds((scrw - 100) / 2, (scrh - 40) / 2, 100, 40);
		btn.addActionListener(e -> StartGame());
		panel.add(btn);
		
		
	}
	
	
}
