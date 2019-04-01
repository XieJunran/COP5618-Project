package cop5618;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import cop5618.utility.Game;

@SuppressWarnings("serial")
public class SinglePlayerClient extends JFrame {
	
	private static final int scrw = 800;
	private static final int scrh = 800;
	
	private JFrame play;
	private Game game = null;
	
	class CheckLive implements Runnable {
		
		public void run() {
			
			while(Game.isLive) {
				
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
		setVisible(false);
		
		game = new Game();
		
		play.setContentPane(game);
		play.setBounds(game.getBounds());
		play.setVisible(true);
		play.setResizable(false);
		
		game.requestFocus();
		new Thread(new CheckLive()).start();
		
	}
	
	private SinglePlayerClient() {
		
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
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(() -> {
			
            try {
            	
                SinglePlayerClient frame = new SinglePlayerClient();
                frame.setResizable(false);
                frame.setVisible(true);
                
            } catch (Exception e) {
            	
                e.printStackTrace();
                
            }
            
        });
		
	}
	
}
