package cop5618.utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MultiPlayerGame extends JPanel {
	
	// width and height for all image except for missile
	private static final int imgw = 32;
	private static final int imgh = 32;
	
	// width and height for missiles
	private static final int missilew = 16;
	private static final int missileh = 16;
	
	// width and height for the screens
	private static final int scrw = 800;
	private static final int scrh = 800;
	
	// images cache for all objects
	private static Image Images[] = new Image[24];
	
	// read all image in to the image cache
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
	
	// constants for objects in 2D-array representing the map
	public static final int NOTHING = 0;
	public static final int WALL = -1;						
	public static final int STELL_WALL = -2;
	public static final int WATER = -3;
	public static final int WATER_AND_MISSILE = -4;
	public static final int MISSILE = -5;
	
	public static final int PLAYER_ONE_LEFT = 10;
	public static final int PLAYER_ONE_RIGHT = 11;
	public static final int PLAYER_ONE_UP = 12;
	public static final int PLAYER_ONE_DOWN = 13;
	
	public static final int PLAYER_TWO_LEFT = 20;
	public static final int PLAYER_TWO_RIGHT = 21;
	public static final int PLAYER_TWO_UP = 22;
	public static final int PLAYER_TWO_DOWN = 23;
	
	public static final int PLAYER_THREE_LEFT = 30;
	public static final int PLAYER_THREE_RIGHT = 31;
	public static final int PLAYER_THREE_UP = 32;
	public static final int PLAYER_THREE_DOWN = 33;
	
	public static final int PLAYER_FOUR_LEFT = 40;
	public static final int PLAYER_FOUR_RIGHT = 41;
	public static final int PLAYER_FOUR_UP = 42;
	public static final int PLAYER_FOUR_DOWN = 43;
	
	public static final int PLAYER_FIVE_LEFT = 50;
	public static final int PLAYER_FIVE_RIGHT = 51;
	public static final int PLAYER_FIVE_UP = 52;
	public static final int PLAYER_FIVE_DOWN = 53;
	
	// constants for the size of the battle field
	private static final int BF_SIZE = 24;
	
	private int[][] field;
	
	private Image OffScreenImage;
	
	private DataInputStream in;
	private DataOutputStream out;
	
	// integer constants for direction
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	
	private int direction;
	
	private boolean moved;
	private boolean fired;
	
	private boolean isLive;
	
	// KeyBoard Listener class used to update the status of the game
	private class KeyBoardListener extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			
			super.keyPressed(e);
			
			int key = e.getKeyCode();
			
			switch(key) {
			case KeyEvent.VK_UP : {
				
				setMoved(true);
				setDirection(UP);
				// System.out.println("Moved up");
				
			}
			break;
			
			case KeyEvent.VK_DOWN : {
				
				setMoved(true);
				setDirection(DOWN);
				// System.out.println("Moved down");
				
			}
			break;
			
			case KeyEvent.VK_LEFT : {
				
				setMoved(true);
				setDirection(LEFT);
				// System.out.println("Moved left");
				
			}
			break;
			
			case KeyEvent.VK_RIGHT : {
				
				setMoved(true);
				setDirection(RIGHT);
				// System.out.println("Moved right");
				
			}
			break;
			
			case KeyEvent.VK_SPACE: {
				
				setFired(true);
				// System.out.println("Fired");
				
			}
			break;
			
			default: {}
			
			}
			
	    }
		
	}
	
	class Sender implements Runnable {
		
		public void run() {
			
			System.out.println("Sender Running!");
			
			while(isLive()) {
				
				boolean m = isMoved();
				boolean f = isFired();
				
				// System.out.println("Try to send!");
				
				setMoved(false);
				setFired(false);
				
				if (m && f) {
					
					try {
						
						out.writeInt(direction + 4);
						// System.out.println(direction + 4);
						out.flush();
						
					} catch (IOException e) {
						
						System.err.println("Failed to send message to the Server!");
						e.printStackTrace();
						
					}
					
				} else if (m) {
					
					try {
						
						out.writeInt(direction);
						// System.out.println(direction);
						out.flush();
						
					} catch (IOException e) {
						
						System.err.println("Failed to send message to the Server!");
						e.printStackTrace();
						
					}
					
				} else if (f) {
					
					try {
						
						out.writeInt(9);
						// System.out.println(9);
						out.flush();
						
					} catch (IOException e) {
						
						System.err.println("Failed to send message to the Server!");
						e.printStackTrace();
						
					}
					
				} else {
					
					try {
						
						out.writeInt(0);
						// System.out.println(0);
						out.flush();
						
					} catch (IOException e) {
						
						System.err.println("Failed to send message to the Server!");
						e.printStackTrace();
						
					}
					
				}
				
				try {
					
					Thread.sleep(100);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		}
		
	}
	
	class Receiver implements Runnable {
		
		public void run() {
			
			while(isLive()) {
				
				try {
					
					updateField();
					repaint();
					// System.out.println("Map got!");
					
				} catch (IOException e) {
					
					System.err.println("Cannot get battlefield from server !");
					e.printStackTrace();
					
				}
				
				try {
					
					Thread.sleep(100);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		}
		
	}
	
	public MultiPlayerGame (DataInputStream input, DataOutputStream output) {
		
		in = input;
		out = output;
		field = new int[BF_SIZE][BF_SIZE];
		
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		setBounds(0, 0, scrw, scrh);
		setLayout(null);
		
		setLive(true);
		
		addKeyListener(new KeyBoardListener());
		
		new Thread(new Sender()).start();
		new Thread(new Receiver()).start();
		
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
	
	synchronized public void paint (Graphics g) {
		
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		for (int i = 0; i < BF_SIZE; i++) {
			
			for (int j = 0; j < BF_SIZE; j++) {
				
				switch (field[j][BF_SIZE - 1 - i]) {
					
					case NOTHING : {
						
					}
					break;
					
					case MISSILE : {
						
						g2d.drawImage(
								Images[23],
								j * imgw + (imgw - missilew) / 2,
								i * imgh + (imgh - missileh) / 2,
								missilew,
								missileh,
								null
						);
						
					}
					break;
					
					case WALL: {
						
						g2d.drawImage(
								Images[20],
								j * imgw,
								i * imgh,
								imgw,
								imgh,
								null
						);
						
					}
					break;
					
					case STELL_WALL : {
						
						g2d.drawImage(
								Images[21],
								j * imgw,
								i * imgh,
								imgw,
								imgh,
								null
						);
						
					}
					break;
					
					case WATER : {
						
						g2d.drawImage(
								Images[22],
								j * imgw,
								i * imgh,
								imgw,
								imgh,
								null
						);
						
					}
					break;
					
					case WATER_AND_MISSILE : {
						
						g2d.drawImage(
								Images[22],
								j * imgw,
								i * imgh,
								imgw,
								imgh,
								null
						);
						
						g2d.drawImage(
								Images[23],
								j * imgw + (imgw - missilew) / 2,
								i * imgh + (imgh - missileh) / 2,
								missilew,
								missileh,
								null
						);
						
					}
					break;
					
					default: {
						
						if ((field[j][BF_SIZE - 1 - i] >= 10 && field[j][BF_SIZE - 1 - i] <= 13) ||
							(field[j][BF_SIZE - 1 - i] >= 20 && field[j][BF_SIZE - 1 - i] <= 23) ||
							(field[j][BF_SIZE - 1 - i] >= 30 && field[j][BF_SIZE - 1 - i] <= 33) ||
							(field[j][BF_SIZE - 1 - i] >= 40 && field[j][BF_SIZE - 1 - i] <= 43) ||
							(field[j][BF_SIZE - 1 - i] >= 50 && field[j][BF_SIZE - 1 - i] <= 53)) {
							
							int id = field[j][BF_SIZE - 1 - i] / 10 - 1;
							int d = field[j][BF_SIZE - 1 - i] % 10;
							
							g2d.drawImage(
									Images[id * 4 + d],
									j * imgw,
									i * imgh,
									imgw,
									imgh,
									null
							);
							
						} else {
							
							System.err.println(
									"Wrong map representation for " +
									field[j][BF_SIZE - 1 - i] +
									" at " + i + ", " + j + " !"
							);
							
						}
					
					}
				
				}
				
			}
			
		}
		
	}
	
	synchronized public void updateField() throws IOException {
		
		for (int i = 0; i < BF_SIZE; i++) {
			
			for (int j = 0; j < BF_SIZE; j++) {
				
				field[i][j] = in.readInt();
				System.out.print(field[i][j] + " ");
				
			}
				
		}
		
	}
	
	synchronized public int getDirection() {
		
		return direction;
		
	}

	synchronized public void setDirection(int d) {
		
		direction = d;
		
	}

	synchronized public boolean isMoved() {
		
		return moved;
		
	}

	synchronized public void setMoved(boolean m) {
		
		moved = m;
		
	}

	synchronized public boolean isFired() {
		
		return fired;
		
	}

	synchronized public void setFired(boolean f) {
		
		fired = f;
		
	}

	synchronized public boolean isLive() {
		
		return isLive;
		
	}

	synchronized public void setLive(boolean l) {
		
		isLive = l;
		
	}
	
}
