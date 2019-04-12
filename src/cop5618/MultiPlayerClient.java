package cop5618;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import cop5618.utility.MultiPlayerGame;

@SuppressWarnings("serial")
public class MultiPlayerClient extends JFrame {
	
	private static final int SERVER_PORT = 5618;
	private static final int CLIENT_PORT = 5619;
	
	private static final int scrw = 780;
	private static final int scrh = 800;
	
	private String serverip;
	
	private Socket socket;
	private DataOutputStream out;
	
	private ServerSocket listener;
	private DataInputStream in;
	
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
			
			try {
				
				in.close();
				out.close();
				socket.close();
				listener.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
			game.removeAll();
			play.remove(game);
			play.dispose();
			
			play = null;
			game = null;
			
			setVisible(true);
			
		}
		
	}
	
	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
		
		try {
			
			InetAddress candidateAddress = null;
			
			for (
				Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
				ifaces.hasMoreElements();
				
			) {
				
				NetworkInterface iface = ifaces.nextElement();
				
				for (
					Enumeration<InetAddress> inetAddrs = iface.getInetAddresses();
					inetAddrs.hasMoreElements();
					
				) {
					
					InetAddress inetAddr = inetAddrs.nextElement();
					
					if (!inetAddr.isLoopbackAddress()) {
						
						if(inetAddr.isSiteLocalAddress()) {
							
							return inetAddr;
							
						} else if (candidateAddress == null){
							
							candidateAddress = inetAddr;
							
						}
						
					}
					
				}
				
			}
			
			if(candidateAddress != null) {
				
				return candidateAddress;
				
			}
			
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			
			if (jdkSuppliedAddress == null) {
				
				throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
				
			}
			
			return jdkSuppliedAddress;
			
		} catch (Exception e) {
			
			UnknownHostException ue = new UnknownHostException ("Fail to determine LAN Addres: " + e);
			ue.initCause(e);
			
			throw ue;
			
		}
		
	}

	private void StartGame () {
		
		play = new JFrame("Fights!");
		play.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		try {
			
			System.out.println("Creating Socket to " + serverip + " !");
			socket = new Socket(serverip, SERVER_PORT);
			listener = new ServerSocket(CLIENT_PORT);
			
			out = new DataOutputStream(socket.getOutputStream());
			out.flush();
			
			System.out.println("HandShaking with the server!");
			InetAddress localhost = getLocalHostLANAddress();
			
		 	String handShaking = "BATTLECITY";
			
			String IP = localhost.getHostAddress().trim();
			System.out.println(IP);
			int len = IP.length();
			
			out.write(handShaking.getBytes());
			out.writeInt(len);
			out.write(IP.getBytes());
			out.flush();
			
			in = new DataInputStream(listener.accept().getInputStream());
			
			byte[] shakeBack = new byte[10];
			in.read(shakeBack);
			String shakeBackStr = new String(shakeBack);
			System.out.println(shakeBackStr);
			
			if(!shakeBackStr.equals("BATTLECITY")) {
								
				throw new Exception("not the right server");
				
			}
			
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
			
		} catch (Exception e) {
			
			e.printStackTrace();
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
