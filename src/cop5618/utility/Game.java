package cop5618.utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class Game extends JPanel {
	
	private static Image Images[] = new Image[24];
	
	private Image OffScreenImage;
	
	private int tankid = 1;
	
	private BattleField battlefield;
	
	static {
		
		Images[0] = new ImageIcon(Game.class.getResource("/img/P1L.gif")).getImage();
		Images[1] = new ImageIcon(Game.class.getResource("/img/P1R.gif")).getImage();
		Images[2] = new ImageIcon(Game.class.getResource("/img/P1U.gif")).getImage();
		Images[3] = new ImageIcon(Game.class.getResource("/img/P1D.gif")).getImage();
		Images[4] = new ImageIcon(Game.class.getResource("/img/P2L.gif")).getImage();
		Images[5] = new ImageIcon(Game.class.getResource("/img/P2R.gif")).getImage();
		Images[6] = new ImageIcon(Game.class.getResource("/img/P2U.gif")).getImage();
		Images[7] = new ImageIcon(Game.class.getResource("/img/P2D.gif")).getImage();
		Images[8] = new ImageIcon(Game.class.getResource("/img/P3L.gif")).getImage();
		Images[9] = new ImageIcon(Game.class.getResource("/img/P3R.gif")).getImage();
		Images[10] = new ImageIcon(Game.class.getResource("/img/P3U.gif")).getImage();
		Images[11] = new ImageIcon(Game.class.getResource("/img/P3D.gif")).getImage();
		Images[12] = new ImageIcon(Game.class.getResource("/img/P4L.gif")).getImage();
		Images[13] = new ImageIcon(Game.class.getResource("/img/P4R.gif")).getImage();
		Images[14] = new ImageIcon(Game.class.getResource("/img/P4U.gif")).getImage();
		Images[15] = new ImageIcon(Game.class.getResource("/img/P4D.gif")).getImage();
		Images[16] = new ImageIcon(Game.class.getResource("/img/P5L.gif")).getImage();
		Images[17] = new ImageIcon(Game.class.getResource("/img/P5R.gif")).getImage();
		Images[18] = new ImageIcon(Game.class.getResource("/img/P5U.gif")).getImage();
		Images[19] = new ImageIcon(Game.class.getResource("/img/P5D.gif")).getImage();
		Images[20] = new ImageIcon(Game.class.getResource("/img/walls.gif")).getImage();
		Images[21] = new ImageIcon(Game.class.getResource("/img/steels.gif")).getImage();
		Images[22] = new ImageIcon(Game.class.getResource("/img/water.gif")).getImage();
		Images[23] = new ImageIcon(Game.class.getResource("/img/missile.gif")).getImage();
		
	}
	
	public static boolean isLive = true;
	
	public Game() {
		
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		setBounds(0, 0, 640, 640);
		setLayout(null);
		
		battlefield.AddTank(1);
		battlefield.AddTank(2);
		
		isLive = true;
		
		new Thread(new Draw()).start();
		
		
	}
	
	class Draw implements Runnable {
		
		public void run() {
			
			while(isLive) {
				
				battlefield.UpdateBattleField();
				repaint();
				
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
				
				int item  = battlefield.getCoordItem(i, j);
				
				switch (item) {
				case(BattleField.WALL): {
					
					g2d.drawImage(Images[20], i, j, 10, 10, null);
					
				}
				break;
				
				case(BattleField.STELL_WALL): {
					
					g2d.drawImage(Images[21], i, j, 10, 10, null);
					
				}
				break;
				
				case(BattleField.WATER): {
					
					g2d.drawImage(Images[22], i, j, 10, 10, null);
					
				}
				break;
				
				default: {
					
					Tank tank = battlefield.getTanklist().get(item);
					
					if(tank.isAlive) {
						
						if (tank.direction == Tank.Direction.LEFT) {
							
							g2d.drawImage(Images[(tank.tankID - 1) * 4], i, j, 10, 10, null);
							
						} else if (tank.direction == Tank.Direction.RIGHT) {
							
							g2d.drawImage(Images[(tank.tankID - 1) * 4 + 1], i, j, 10, 10, null);
							
						} else if (tank.direction == Tank.Direction.UP) {
							
							g2d.drawImage(Images[(tank.tankID - 1) * 4 + 2], i, j, 10, 10, null);
							
						} else {
							
							g2d.drawImage(Images[(tank.tankID - 1) * 4 + 3], i, j, 10, 10, null);
							
						}
					
					}
				
				}
				
				}
				
			}
			
		}
		
		Map<Integer, Missile> missilelist = battlefield.getMissilelist();
		
		for (Missile missile: missilelist.values()) {
			
			g2d.drawImage(Images[23], missile.x, missile.y, 10, 10, null);
			
		}
		
	}
	
	synchronized public void update(Graphics g) {
		
		super.update(g);
		
		if (OffScreenImage == null) {
			
			OffScreenImage = this.createImage(640, 640);
			
		}
		
		Graphics gosi = OffScreenImage.getGraphics();
		Color c= gosi.getColor();
		
		gosi.setColor(Color.BLACK);
		gosi.fillRect(0, 0, 640, 640);
		gosi.setColor(c);
		
		g.drawImage(OffScreenImage, 0, 0, null);
		
		paint(gosi);
		
	}
	
}
