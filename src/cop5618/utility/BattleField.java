package cop5618.utility;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

import cop5618.utility.Tank.Direction;
import cop5618.utility.Tank.Type;

public class BattleField implements Runnable {
	
	public static final int WALL = -1;									//
	public static final int STELL_WALL = -2;							//
	public static final int WATER = -3;									//
	public static final int WATER_AND_MISSILE = -4;                     //
	public static final int MISSILE = -5;								//
	
	public static final int BF_SIZE = 24;
	
	public static final int MAX_PLAYER_NUM = 5;
	public static final int MIN_PLAYER_NUM = 2;
	
	private int field[][] = new int[BF_SIZE][BF_SIZE];
	private ReentrantLock fieldLock = new ReentrantLock();
	
	private AtomicInteger playerNum = new AtomicInteger(0);
	
	private List<Tank> tankList = new ArrayList<Tank>();
	private ReentrantLock tankListLock = new ReentrantLock();
	private Map<Integer, Missile> missileList = new HashMap<Integer, Missile>();
	private ReentrantLock missileListLock = new ReentrantLock();
	private List<Client> clientList = new ArrayList<Client>();
	private ReentrantLock clientListLock = new ReentrantLock();
	
	private volatile boolean started = false;
	private volatile boolean ended = false;
	
	public BattleField() {
		field = SafeMapFactory.newMapInstance();
	}
	
	@Override
	public void run() {
		while((!started) || (started && !ended)) {
			System.out.println("Battle Field is running");
			UpdateBattleField();
			int[][] copyOfField = getCopyOfField();
			clientListLock.lock();
			try {
				for(Client client : clientList) {
					client.sendBF(copyOfField);
					System.out.println("Sent bf to " + client.toString());
				}
			}finally {
				clientListLock.unlock();
			}
			try {
				Thread.sleep(10);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	// AddMissile is only used when the fieldLock, tankListLock, missileListLock and genLock of tank are held
	// Actually, there is no need to lock them again
	// But we still require the locks in case of accident
	private void AddMissile (Tank tank) {
		fieldLock.lock();
		tankListLock.lock();
		missileListLock.lock();
		tank.genLock.lock();
		
		try {
			Direction d = tank.getDirection();
			int tankPosX = tank.getPosX();
			int tankPosY = tank.getPosY();
			
			int missilePosX = tankPosX;
			int missilePosY = tankPosY;
			if(d == Direction.LEFT) {
				missilePosX--;
			}else if(d == Direction.RIGHT) {
				missilePosX++;
			}else if(d == Direction.DOWN) {
				missilePosY--;
			}else if(d == Direction.UP) {
				missilePosY++;
			}
			// Boundry checking
			if(missilePosX < 0 || missilePosX >= BF_SIZE || missilePosY < 0 || missilePosY >= BF_SIZE)
				return;
			// Missile checking
			for(Missile otherMissile : missileList.values()) {
				if(missilePosX == otherMissile.x && missilePosY == otherMissile.y)
					return;
			}
			if(field[missilePosX][missilePosY] == 0 || field[missilePosX][missilePosY] == WATER) {
				Missile newMissile = new Missile(tank, this, d, missilePosX, missilePosY);
				missileList.put(newMissile.missileID, newMissile);
			}else if(field[missilePosX][missilePosY] == WALL) {
				field[missilePosX][missilePosY] = 0;
			}else if(field[missilePosX][missilePosY] == STELL_WALL) {
				// The missile can't be generated
			}else {
				for(Iterator<Tank> iterator = tankList.iterator(); iterator.hasNext(); ) {
					Tank t = iterator.next();
					t.genLock.lock();
					try {
						if(t.getPosX() == missilePosX && t.getPosY() == missilePosY && t.getAlive()) {
							t.setAlive(false);
							playerNum.getAndDecrement();
							iterator.remove();
							field[missilePosX][missilePosY] = 0;
						}
					}finally {
						t.genLock.unlock();
					}
				}
			}
		}finally {
			fieldLock.unlock();
			tankListLock.unlock();
			missileListLock.unlock();
			tank.genLock.unlock();
		}
	}
	
	// Update field by moving tanks and missiles
	public void UpdateBattleField () {
		fieldLock.lock();
		tankListLock.lock();
		missileListLock.lock();
		
		try {
		
			Iterator<Map.Entry<Integer, Missile>> iterator = missileList.entrySet().iterator();
			
			while (iterator.hasNext()) {
				Map.Entry<Integer, Missile> entry = iterator.next();
				Missile missile = entry.getValue();
				
				missile.move();
				
				if (missile.x < 0 || missile.x >= BF_SIZE 
						|| missile.y < 0 || missile.y >= BF_SIZE 
						|| field[missile.x][missile.y] == STELL_WALL) {
					iterator.remove();
				}else if (field[missile.x][missile.y] == -1) {
					field[missile.x][missile.y] = 0;
					iterator.remove();
				}else if (field[missile.x][missile.y] > 0) {
					for(Tank tank : tankList) {
						if(tank.getPosX() == missile.x && tank.getPosY() == missile.y) {
							tank.setAlive(false);
							playerNum.decrementAndGet();
							field[missile.x][missile.y] = 0;
							iterator.remove();
							break;
						}
					}
				}
			}
			
			for (Tank tank : tankList) {
				tank.genLock.lock();
				System.out.println("Tank id: " + tank.tankID + " Moved: " + tank.getMoved() + " Alive: " + tank.getAlive());
				try {
					if (tank.getAlive()) {
						if (tank.getMoved()) {
							System.out.println("Tank " + tank.tankID + " is moving");
							Direction d = tank.getDirection();
							int posX = tank.getPosX();
							int posY = tank.getPosY();
							System.out.println("PosX: " + posX + " PosY: " + posY + " Direction: " + d);
							switch (d) {
							case LEFT: {
								if (posX != 0 && field[posX - 1][posY] == 0) {
									field[posX][posY] = 0;
									field[--posX][posY] = tank.tankID;
								}
							}
							break;
							case RIGHT: {
								if (posX != BF_SIZE - 1 && field[posX + 1][posY] == 0) {
									field[posX][posY] = 0;
									field[++posX][posY] = tank.tankID;
								}
							}
							break;
							case DOWN: {
								if (posY != 0 && field[posX][posY - 1] == 0) {
									field[posX][posY] = 0;
									field[posX][--posY] = tank.tankID;
								}
							}
							break;
							case UP: {
								if (posY != BF_SIZE - 1 && field[posX][posY + 1] == 0) {
									field[posX][posY] = 0;
									field[posX][++posY] = tank.tankID;
								}
							}
							break;
							}
							tank.setPosX(posX);
							tank.setPosY(posY);
							tank.clearMovement();
						}
						
						if (tank.getFired()) {
							AddMissile(tank);
							tank.clearFired();
						}
					}
				}
				finally {
					tank.genLock.unlock();
				}
			}
		}
		finally {
			fieldLock.unlock();
			tankListLock.unlock();
			missileListLock.unlock();
		}
	}
	
	// Add a tank into tanklist
	public Tank AddTank(Client client) {
		int bornPosX = 0, bornPosY = 0;
		fieldLock.lock();
		tankListLock.lock();
		clientListLock.lock();
		try {
			if(field[0][0] == 0) {
				bornPosX = 0;
				bornPosY = 0;
			}else if(field[BF_SIZE - 1][0] == 0) {
				bornPosX = BF_SIZE - 1;
				bornPosY = 0;
			}else if(field[0][BF_SIZE - 1] == 0) {
				bornPosX = 0;
				bornPosY = BF_SIZE - 1;
			}else {
				bornPosX = BF_SIZE - 1;
				bornPosY = BF_SIZE - 1;
			}
			Tank tank = SafeTankFactory.newTankInstance(this, Type.values()[playerNum.getAndIncrement()], bornPosX, bornPosY);
			field[bornPosX][bornPosY] = tank.tankID;
			tankList.add(tank);
			clientList.add(client);
			if(playerNum.get() >= MIN_PLAYER_NUM)
				started = true;
			System.out.println("A new tank has been added");
			return tank;
		}finally {
			fieldLock.unlock();
			tankListLock.unlock();
			clientListLock.unlock();
		}
	}
	
	private int[][] getCopyOfField () {
		fieldLock.lock();
		missileListLock.lock();
		tankListLock.lock();
		try {
			int[][] copyOfField = new int[BF_SIZE][BF_SIZE];
			for(Missile missile : missileList.values()) {
				if(field[missile.x][missile.y] == WATER) {
					copyOfField[missile.x][missile.y] = WATER_AND_MISSILE;
				}else {
					copyOfField[missile.x][missile.y] = MISSILE;
				}
			}
			for(int i = 0; i < BF_SIZE; i++) {
				for(int j = 0; j < BF_SIZE; j++) {
					if(copyOfField[i][j] == WATER_AND_MISSILE || copyOfField[i][j] == MISSILE)
						continue;
					if(field[i][j] > 0){
						for(Tank tank : tankList) {
							if(tank.tankID == field[i][j]) {
								int val = tank.type.getValue() * 10 + tank.getDirection().getValue(); 
								copyOfField[i][j] = val;
								break;
							}
						}
					}else {
						copyOfField[i][j] = field[i][j];
					}
				}
			}
			return copyOfField;
		}finally {
			fieldLock.unlock();
			missileListLock.unlock();
			tankListLock.unlock();
		}
	}
	
	public boolean isEnded() {
		return ended;
	}
	
}