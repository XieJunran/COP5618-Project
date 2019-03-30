package cop5618.utility;

import java.util.ArrayList;

public class BattleField {
	
	// flags
	final static int WALL = -1;
	final static int STELL_WALL = -2;
	final static int WATER = -3;
	
	final static int PLAYER_ONE = 1;
	final static int PLAYER_TWO = 2;
	final static int PLAYER_THREE = 3;
	final static int PLAYER_FOUR = 4;
	
	private int field[][] = new int[64][64];
	
	private ArrayList<Tank> tanklist;
	private ArrayList<Missile> missilelist;
	
	public void AddMissile (Missile missile) {
		
		
		
	}
	
	// Add a tank into tanklist
	public void AddTank (Tank tank) {
		
		
		
	}
	
	// Update field by moving tanks and missiles
	public void UpdateBattleField () {
		
		
		
	}
	
	public int[][] getField () {
		
		return field;
		
	}
	
	// Get what is in the coord
	public int getCoordItem (int x, int y) {
		
		return field[x][y];
		
	}
	
<<<<<<< HEAD
	// Get tanklist
	public ArrayList<Tank> getTanklist () {
		
		return tanklist;
		
	}
	
=======
>>>>>>> aade1cb548a56a86397a934c2eac4b222d33dcad
}
