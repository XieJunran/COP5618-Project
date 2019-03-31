package cop5618.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BattleField {
	
	// flags
	final static int WALL = -1;
	final static int STELL_WALL = -2;
	final static int WATER = -3;
	
	final static int PLAYER_ONE = 1;
	final static int PLAYER_TWO = 2;
	final static int PLAYER_THREE = 3;
	final static int PLAYER_FOUR = 4;
	
	final static int BFSize = 64;
	
	private int field[][] = new int[BFSize][BFSize];
	
	private List<Tank> tanklist= new ArrayList<Tank>();
	private Map<Integer, Missile> missilelist = new HashMap<Integer, Missile>();
	/*
	public void AddMissile (Missile missile) {
		
		
		
	}
	*/
	// Add a tank into tanklist
	public void AddTank (int tankid) {
		int x, y;
		switch (tankid) {
		
		case 0: {
			x = 1;
			y = 1;
		}
		break;
		case 1: {
			x = 1;
			y = BFSize - 2;
		}
		break;
		case 2: {
			x = BFSize - 2;
			y = 1;
		}
		break;
		case 3: {
			x = BFSize - 2;
			y = BFSize - 2;
		}
		break;
		default: {
			x = 1;
			y = 1;
		}
		}
		Tank newTank = new Tank(this, x, y);
		tanklist.add(newTank);
		
	}
	
	// Update field by moving tanks and missiles
	public void UpdateBattleField () {
		
		Iterator<Map.Entry<Integer, Missile>> entries = missilelist.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<Integer, Missile> entry = entries.next();
			Missile missile = entry.getValue();
			missile.move();
			if (missile.x < 0 || missile.x >= BFSize || missile.y < 0 || missile.y >= BFSize || field[missile.x][missile.y] == -2) missilelist.remove(entry.getKey());
			else if (field[missile.x][missile.y] == -1) {
				field[missile.x][missile.y] = 0;
				missilelist.remove(entry.getKey());
			}
			else if (field[missile.x][missile.y] > 0) {
				tanklist.get(field[missile.x][missile.y] - 1).isAlive = false;
				field[missile.x][missile.y] = 0;
				missilelist.remove(entry.getKey());
			}
		}
		
		for (Tank tank : tanklist) {
			if (tank.isAlive) {
				if (tank.moved) {
					switch (tank.direction) {
					case UP: {
						if (tank.x != 0 && tank.x - 1 == 0) {
							field[tank.x][tank.y] = 0;
							--tank.x;
						}
					}
					break;
					case DOWN: {
						if (tank.x != BFSize - 1 && tank.x + 1 == 0) {
							field[tank.x][tank.y] = 0;
							++tank.x;
						}
					}
					break;
					case LEFT: {
						if (tank.y != 0 && tank.y - 1 == 0) {
							field[tank.x][tank.y] = 0;
							--tank.y;
						}
					}
					break;
					case RIGHT: {
						if (tank.y != BFSize - 1 && tank.y - 1 == 0) {
							field[tank.x][tank.y] = 0;
							++tank.y;
						}
					}
					break;
					}
				}
				
				if (tank.fired) {
					switch (tank.direction) {
					case UP: {
						if (tank.x != 0) {
							if (field[tank.x - 1][tank.y] == 0 || field[tank.x - 1][tank.y] == -3) {
								Missile newMissile = new Missile(tank, this, tank.direction, tank.x - 1, tank.y);
								missilelist.put(newMissile.missileID, newMissile);
							}
							else if (field[tank.x - 1][tank.y] == -1) {
								field[tank.x - 1][tank.y] = 0;
							}
							else if (field[tank.x - 1][tank.y] > 0) {
								tanklist.get(field[tank.x - 1][tank.y] - 1).isAlive = false;
								field[tank.x - 1][tank.y] = 0;
							}
						}
					}
					break;
					case DOWN: {
						if (tank.x != BFSize - 1) {
							if (field[tank.x + 1][tank.y] == 0 || field[tank.x + 1][tank.y] == -3) {
								Missile newMissile = new Missile(tank, this, tank.direction, tank.x + 1, tank.y);
								missilelist.put(newMissile.missileID, newMissile);
							}
							else if (field[tank.x + 1][tank.y] == -1) {
								field[tank.x + 1][tank.y] = 0;
							}
							else if (field[tank.x + 1][tank.y] > 0) {
								tanklist.get(field[tank.x + 1][tank.y] - 1).isAlive = false;
								field[tank.x + 1][tank.y] = 0;
							}
						}
					}
					break;
					case LEFT: {
						if (tank.y != 0) {
							if (field[tank.x][tank.y - 1] == 0 || field[tank.x][tank.y - 1] == -3) {
								Missile newMissile = new Missile(tank, this, tank.direction, tank.x, tank.y - 1);
								missilelist.put(newMissile.missileID, newMissile);
							}
							else if (field[tank.x][tank.y - 1] == -1) {
								field[tank.x][tank.y - 1] = 0;
							}
							else if (field[tank.x][tank.y - 1] > 0) {
								tanklist.get(field[tank.x][tank.y - 1] - 1).isAlive = false;
								field[tank.x][tank.y - 1] = 0;
							}
						}
					}
					break;
					case RIGHT: {
						if (tank.y != BFSize - 1) {
							if (field[tank.x][tank.y + 1] == 0 || field[tank.x][tank.y + 1] == -3) {
								Missile newMissile = new Missile(tank, this, tank.direction, tank.x, tank.y + 1);
								missilelist.put(newMissile.missileID, newMissile);
							}
							else if (field[tank.x][tank.y + 1] == -1) {
								field[tank.x][tank.y + 1] = 0;
							}
							else if (field[tank.x][tank.y + 1] > 0) {
								tanklist.get(field[tank.x][tank.y + 1] - 1).isAlive = false;
								field[tank.x][tank.y + 1] = 0;
							}
						}
					}
					break;
					}
				}
			}
		}
		
	}
	/*
	public int[][] getField () {
		
		return field;
		
	}
	*/
	// Get what is in the coord
	public int getCoordItem (int x, int y) {
		
		return field[x][y];
		
	}
	
	public void setCoordItem (int x, int y, int item) {
		
		field[x][y] = item;
		
	}
	
	// Get tanklist
	public List<Tank> getTanklist () {
		
		return tanklist;
		
	}
	
	public Map<Integer, Missile> getMissilelist() {
		
		return missilelist;
		
	}
	
}
