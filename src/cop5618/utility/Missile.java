package cop5618.utility;

import cop5618.utility.Tank.Direction;

public class Missile {
	
	private static int missileCounter = 0;				//Used to generate a unique missileID
	
	public int missileID;								// A unique ID assigned
	public int tankID;									// the id of the tank that produces it
	public BattleField battleField;						// reference to the battle field
	public Direction direction;							// The direction of missile
	public int x;										// The original x coordinate the missile is generated
	public int y;										// The original y coordinate the missile is generated
	
	Missile(Tank tank, BattleField battleField, Direction direction, int originalX, int originalY){
		this.missileID = ++missileCounter;
		this.tankID = tank.tankID;
		this.battleField = battleField;
		this.direction = tank.direction;
		this.x = originalX;
		this.y = originalY;
	}
	
	public void move() {
		switch(direction) {
		case LEFT:
			x--;
			break;
		case RIGHT:
			x++;
			break;
		case UP:
			y++;
			break;
		case DOWN:
			y--;
			break;
		}
	}
	
}
