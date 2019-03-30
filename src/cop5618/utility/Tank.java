package cop5618.utility;

public class Tank {
	
	public static enum Direction {
		LEFT, RIGHT, UP, DOWN
	}
	
	private static final Direction DEFAULT_DIRECTION = Direction.UP;	// Default deriction when a tank is created
	private static int tankCounter = 0;										// Used to generate a unique tank id
	
	public int tankID;													// An unique id assigned to the tank
	public BattleField battleField;										// Reference to the battle field
	public Direction direction;											// Current direction of the tank
	public boolean moved;												// If the tank is moved in current frame
	public boolean fired;												// If the tank fires in current frame
	public boolean isAlive;												// If the tank is still alive
	
	Tank(BattleField battleField) {
		this.tankID = ++tankCounter;
		this.battleField = battleField;
		this.direction = DEFAULT_DIRECTION;
		this.moved = false;
		this.fired = false;
		this.isAlive = true;
	}
	
	Tank(BattleField battleField, Direction direction) {
		this.tankID = ++tankCounter;
		this.battleField = battleField;
		this.direction = direction;
		this.moved = false;
		this.fired = false;
		this.isAlive = true;
	}
	
	// takes a keyboard input, and move according to it
	public void move(Direction d) {
		direction = d;
		moved = true;
	}
	
	// fire a missile towards the direction
	public void fire() {
		fired = true;
	}

}
