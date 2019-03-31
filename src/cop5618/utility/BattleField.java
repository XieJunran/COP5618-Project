package cop5618.utility;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	private ArrayList<Tank> tanklist;
	private HashMap<Integer, Missile> missilelist;
	/*
	public void AddMissile (Missile missile) {
		
		
		
	}
	*/
	// Add a tank into tanklist
	public void AddTank (int tankid) {
		
		Tank newTank = new Tank(this);
		tanklist.add(newTank);
		
	}
	
	// Update field by moving tanks and missiles
	public void UpdateBattleField () {
		
		for (Integer missileid : missilelist.keySet()) {
			Missile missile = missilelist.get(missileid);
			switch (missile.direction) {
			case UP: {
				if (missile.x == 0 || field[missile.x - 1][missile.y] == -2) missilelist.remove(missileid);
				else if (field[missile.x - 1][missile.y] == -1) {
					field[missile.x - 1][missile.y] = 0;
					missilelist.remove(missileid);
				}
				else if (field[missile.x - 1][missile.y] > 0) {
					tanklist.get(field[missile.x - 1][missile.y]).isAlive = false;
					field[missile.x - 1][missile.y] = 0;
					missilelist.remove(missileid);
				}
				else --missile.x;
			}
			case DOWN: {
				if (missile.x == BFSize - 1 || field[missile.x + 1][missile.y] == -2) missilelist.remove(missileid);
				else if (field[missile.x + 1][missile.y] == -1) {
					field[missile.x + 1][missile.y] = 0;
					field[missile.x + 1][missile.y] = 0;
					missilelist.remove(missileid);
				}
				else if (field[missile.x + 1][missile.y] > 0) {
					tanklist.get(field[missile.x + 1][missile.y]).isAlive = false;
					missilelist.remove(missileid);
				}
				else ++missile.x;
			}
			case LEFT: {
				if (missile.y == 0 || field[missile.x][missile.y - 1] == -2) missilelist.remove(missileid);
				else if (field[missile.x][missile.y - 1] == -1) {
					field[missile.x][missile.y - 1] = 0;
					missilelist.remove(missileid);
				}
				else if (field[missile.x][missile.y - 1] > 0) {
					tanklist.get(field[missile.x][missile.y - 1]).isAlive = false;
					field[missile.x][missile.y - 1] = 0;
					missilelist.remove(missileid);
				}
				else --missile.y;
			}
			case RIGHT: {
				if (missile.y == BFSize - 1 || field[missile.x][missile.y + 1] == -2) missilelist.remove(missileid);
				else if (field[missile.x][missile.y + 1] == -1) {
					field[missile.x][missile.y + 1] = 0;
					missilelist.remove(missileid);
				}
				else if (field[missile.x][missile.y + 1] > 0) {
					tanklist.get(field[missile.x][missile.y + 1]).isAlive = false;
					field[missile.x][missile.y + 1] = 0;
					missilelist.remove(missileid);
				}
				else ++missile.y;
			}
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
					case DOWN: {
						if (tank.x != BFSize - 1 && tank.x + 1 == 0) {
							field[tank.x][tank.y] = 0;
							++tank.x;
						}
					}
					case LEFT: {
						if (tank.y != 0 && tank.y - 1 == 0) {
							field[tank.x][tank.y] = 0;
							--tank.y;
						}
					}
					case RIGHT: {
						if (tank.y != BFSize - 1 && tank.y - 1 == 0) {
							field[tank.x][tank.y] = 0;
							++tank.y;
						}
					}
					}
				}
				
				if (tank.fired) {
					switch (tank.direction) {
					case UP: {
						if (tank.x != 0) {
							if (field[tank.x - 1][tank.y] == 0 || field[tank.x - 1][tank.y] == -3) {
								Missile newMissile = new Missile(tank.tankid, this, tank.direction, tank.x - 1, tank.y);
								missilelist.add(newMissile.missileid, newMissile);
							}
							else if (field[tank.x - 1][tank.y] == -1) {
								field[tank.x - 1][tank.y] = 0;
							}
							else if (field[tank.x - 1][tank.y] > 0) {
								tanklist.get(field[tank.x - 1][tank.y]).isAlive = false;
								field[tank.x - 1][tank.y] = 0;
							}
						}
					}
					case DOWN: {
						if (tank.x != BFSize - 1) {
							if (field[tank.x + 1][tank.y] == 0 || field[tank.x + 1][tank.y] == -3) {
								Missile newMissile = new Missile(tank.tankid, this, tank.direction, tank.x + 1, tank.y);
								missilelist.add(newMissile.missileid, newMissile);
							}
							else if (field[tank.x + 1][tank.y] == -1) {
								field[tank.x + 1][tank.y] = 0;
							}
							else if (field[tank.x + 1][tank.y] > 0) {
								tanklist.get(field[tank.x + 1][tank.y]).isAlive = false;
								field[tank.x + 1][tank.y] = 0;
							}
						}
					}
					case LEFT: {
						if (tank.y != 0) {
							if (field[tank.x][tank.y - 1] == 0 || field[tank.x][tank.y - 1] == -3) {
								Missile newMissile = new Missile(tank.tankid, this, tank.direction, tank.x, tank.y - 1);
								missilelist.add(newMissile.missileid, newMissile);
							}
							else if (field[tank.x][tank.y - 1] == -1) {
								field[tank.x][tank.y - 1] = 0;
							}
							else if (field[tank.x][tank.y - 1] > 0) {
								tanklist.get(field[tank.x][tank.y - 1]).isAlive = false;
								field[tank.x][tank.y - 1] = 0;
							}
						}
					}
					case RIGHT: {
						if (tank.y != BFSize - 1) {
							if (field[tank.x][tank.y + 1] == 0 || field[tank.x][tank.y + 1] == -3) {
								Missile newMissile = new Missile(tank.tankid, this, tank.direction, tank.x, tank.y + 1);
								missilelist.add(newMissile.missileid, newMissile);
							}
							else if (field[tank.x][tank.y + 1] == -1) {
								field[tank.x][tank.y + 1] = 0;
							}
							else if (field[tank.x][tank.y + 1] > 0) {
								tanklist.get(field[tank.x][tank.y + 1]).isAlive = false;
								field[tank.x][tank.y + 1] = 0;
							}
						}
					}
					}
				}
			}
		}
		
	}
	
	public int[][] getField () {
		
		return field;
		
	}
	
	// Get what is in the coord
	public int getCoordItem (int x, int y) {
		
		return field[x][y];
		
	}
	
	// Get tanklist
	public ArrayList<Tank> getTanklist () {
		
		return tanklist;
		
	}
	
}
