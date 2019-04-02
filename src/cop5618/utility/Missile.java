package cop5618.utility;

import java.util.concurrent.atomic.*;

import cop5618.utility.Tank.Direction;

public class Missile {
	
	private static AtomicInteger missileCounter = new AtomicInteger(0);		//Used to generate a unique missileID
	
	public final int missileID;												// A unique ID assigned
	public final int tankID;												// the id of the tank that produces it
	public final BattleField battleField;									// reference to the battle field
	public final Direction direction;										// The direction of missile
	public volatile int x;													// The original x coordinate the missile is generated
	public volatile int y;													// The original y coordinate the missile is generated
	
	Missile(Tank tank, BattleField battleField, Direction direction, int originalX, int originalY){
		this.missileID = missileCounter.incrementAndGet();
		this.tankID = tank.tankID;
		this.battleField = battleField;
		this.direction = tank.getDirection();
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
