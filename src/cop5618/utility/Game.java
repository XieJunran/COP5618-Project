package cop5618.utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Game extends JPanel {
	
	private static final int imgw = 40;
	private static final int imgh = 40;
	
	private static final int missilew = 10;
	private static final int missileh = 10;
	
	private static final int scrw = 800;
	private static final int scrh = 800;
	
	private static Image Images[] = new Image[24];
	
	private Image OffScreenImage;
	
	private BattleField battlefield = new BattleField();
	
	static {
		
		Images[0] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P1L.gif")).getImage();
		Images[1] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P1R.gif")).getImage();
		Images[2] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P1U.gif")).getImage();
		Images[3] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P1D.gif")).getImage();
		Images[4] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P2L.gif")).getImage();
		Images[5] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P2R.gif")).getImage();
		Images[6] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P2U.gif")).getImage();
		Images[7] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P2D.gif")).getImage();
		Images[8] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P3L.gif")).getImage();
		Images[9] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P3R.gif")).getImage();
		Images[10] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P3U.gif")).getImage();
		Images[11] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P3D.gif")).getImage();
		Images[12] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P4L.gif")).getImage();
		Images[13] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P4R.gif")).getImage();
		Images[14] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P4U.gif")).getImage();
		Images[15] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P4D.gif")).getImage();
		Images[16] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P5L.gif")).getImage();
		Images[17] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P5R.gif")).getImage();
		Images[18] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P5U.gif")).getImage();
		Images[19] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/P5D.gif")).getImage();
		Images[20] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/walls.gif")).getImage();
		Images[21] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/steels.gif")).getImage();
		Images[22] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/water.gif")).getImage();
		Images[23] = new ImageIcon(Game.class.getResource("/cop5618/resources/img/missile.gif")).getImage();
		
	}
	
	public static boolean isLive = true;
	
	public Game() {
		
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		setBounds(0, 0, scrw, scrh);
		setLayout(null);
		
		battlefield.AddTank(1);
		battlefield.AddTank(2);
		
		addKeyListener(new KeyBoardListener());
		
		isLive = true;
		
		new Thread(new Draw()).start();
		
	}
	
	class Draw implements Runnable {
		
		public void run() {
			
			while(isLive) {
				
				battlefield.UpdateBattleField();
				if (battlefield.getTanklist().size() == 1) {
					
					ShutDown();
					
				} else {
					
					repaint();
					
				}
				
				try {
					
					Thread.sleep(100);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		}
		
	}
	
	synchronized public void paint (Graphics g) {
		
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		for (int i = 0; i < BattleField.BFSize; i++) {
			
			for (int j = 0; j < BattleField.BFSize; j++) {
				
				int item  = battlefield.getCoordItem(j, BattleField.BFSize - 1 - i);
				
				switch (item) {
				case(BattleField.WALL): {
					
					g2d.drawImage(Images[20], j * 10, i * 10, imgw, imgh, null);
					
				}
				break;
				
				case(BattleField.STELL_WALL): {
					
					g2d.drawImage(Images[21], j * 10, i * 10, imgw, imgh, null);
					
				}
				break;
				
				case(BattleField.WATER): {
					
					g2d.drawImage(Images[22], j * 10, i * 10, imgw, imgh, null);
					
				}
				break;
				
				default: {
					
					if (item <= 0 || item > 5) break;
					
					Tank tank = battlefield.getTanklist().get(item - 1);
					
					System.out.println(item + " at " + j + " " + i);
					
					if(tank.isAlive) {
						
						if (tank.direction == Tank.Direction.LEFT) {
							
							g2d.drawImage(Images[(item - 1) * 4], j * 10, i * 10, imgw, imgh, null);
							// g2d.drawImage(Images[(item - 1) * 4], i, j, imgw, imgh, null);
							
						} else if (tank.direction == Tank.Direction.RIGHT) {
							
							g2d.drawImage(Images[(item - 1) * 4 + 1], j * 10, i * 10, imgw, imgh, null);
							// g2d.drawImage(Images[(item - 1) * 4 + 1], i, j, imgw, imgh, null);
							
						} else if (tank.direction == Tank.Direction.UP) {
							
							g2d.drawImage(Images[(item - 1) * 4 + 2], j * 10, i * 10, imgw, imgh, null);
							// g2d.drawImage(Images[(item - 1) * 4 + 2], i, j, imgw, imgh, null);
							
						} else {
							
							g2d.drawImage(Images[(item - 1) * 4 + 3], j * 10, i * 10, imgw, imgh, null);
							// g2d.drawImage(Images[(item - 1) * 4 + 3], i, j, imgw, imgh, null);
							
						}
					
					}
				
				}
				
				}
				
			}
			
		}
		
		Map<Integer, Missile> missilelist = battlefield.getMissilelist();
		
		for (Missile missile: missilelist.values()) {

			g2d.drawImage(Images[23], missile.x * 10, (BattleField.BFSize - 1 - missile.y) * 10, missilew, missileh, null);
			
		}
		
	}
	
	private boolean isInRange(int x, int y) {
		return x >= 0 && x < BattleField.BFSize && y >= 0 && y < BattleField.BFSize;
	}
	
	synchronized public void update(Graphics g) {
		
		super.update(g);
		
		if (OffScreenImage == null) {
			
			OffScreenImage = this.createImage(scrw, scrh);
			
		}
		
		Graphics gosi = OffScreenImage.getGraphics();
		Color c = gosi.getColor();
		
		gosi.setColor(Color.BLACK);
		gosi.fillRect(0, 0, scrw, scrh);
		gosi.setColor(c);
		
		g.drawImage(OffScreenImage, 0, 0, null);
		
		paint(gosi);
		
	}
	
	synchronized void ShutDown() {
		
		isLive = false;
		
	}
	
	private class KeyBoardListener extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			
			super.keyPressed(e);
			
			int key = e.getKeyCode();
			
			Tank p1 = battlefield.getTanklist().get(1);
			
			switch(key) {
			case KeyEvent.VK_UP : {
				
				p1.move(Tank.Direction.UP);
				
			}
			break;
			
			case KeyEvent.VK_DOWN : {
				
				p1.move(Tank.Direction.DOWN);
				
			}
			break;
			
			case KeyEvent.VK_LEFT : {
				
				p1.move(Tank.Direction.LEFT);
				
			}
			break;
			
			case KeyEvent.VK_RIGHT : {
				
				p1.move(Tank.Direction.RIGHT);
				
			}
			break;
			
			case KeyEvent.VK_SPACE: {
				
				p1.fire();
				
			}
			break;
			
			default:{}
			
			}
			
	    }
		
	}
	
}