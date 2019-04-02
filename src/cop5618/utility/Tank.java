package cop5618.utility;

import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class Tank {
	
	public static enum Direction {
		LEFT(0), RIGHT(1), UP(2), DOWN(3);
		private final int value;
		private Direction(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	public static enum Type{
		LIGHT_YELLOW(1), LIGHT_GREEN(2), MEDIUM_GREY(3), HEAVY_YELLOW(4), HEAVY_GREEN(5);
		private final int value;
		private Type(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	private static final Direction DEFAULT_DIRECTION = Direction.UP;	// Default deriction when a tank is created
	private static AtomicInteger tankCounter = new AtomicInteger(0);	// Used to generate a unique tank id
	
	public final int tankID;											// An unique id assigned to the tank
	public final Type type;
	public final BattleField battleField;								// Reference to the battle field
	private Direction direction;										// Current direction of the tank
	private volatile int x;												// The current x coordinate of the tank
	private volatile int y;												// The current y coordinate of the tank
	
	private volatile boolean moved = false;								// If the tank is moved in current frame
	private volatile boolean fired = false;								// If the tank fires in current frame
	private volatile boolean isAlive = false;							// If the tank is still alive
	
	public final ReentrantLock genLock = new ReentrantLock();			// Guard the direction, moved, fired, isAlive, x and y
	
	
	Tank(BattleField battleField, Type type, int x, int y) {
		this.tankID = tankCounter.incrementAndGet();
		this.battleField = battleField;
		this.type = type;
		this.direction = DEFAULT_DIRECTION;
		this.x = x;
		this.y = y;
	}
	
	Tank(BattleField battleField, Type type, Direction direction, int x, int y) {
		this.tankID = tankCounter.incrementAndGet();
		this.battleField = battleField;
		this.type = type;
		this.direction = direction;
		this.x = x;
		this.y = y;
	}
	
	// takes a keyboard input, and move according to it
	public void move(Direction d) {
		genLock.lock();
		try {
			direction = d;
			moved = true;
		}
		finally {
			genLock.unlock();
		}
	}
	
	// Used when an update of battle field is done
	public void clearMovement() {
		genLock.lock();
		try {
			moved = false;
		}finally {
			genLock.unlock();
		}
	}
	
	// fire a missile towards the direction
	public void fire() {
		genLock.lock();
		try {
			fired = true;
		}finally {
			genLock.unlock();
		}
	}
	
	// Synchonized reader of direction
	public Direction getDirection() {
		genLock.lock();
		try {
			return direction;
		}finally {
			genLock.unlock();
		}
	}
	
	// Synchonized reader of moved
	public boolean getMoved() {
		genLock.lock();
		try {
			return moved;
		}finally {
			genLock.unlock();
		}
	}
	
	public void clearFired() {
		genLock.lock();
		try {
			fired = false;
		}finally {
			genLock.unlock();
		}
	}
	
	// Synchonized reader of fired
	public boolean getFired() {
		genLock.lock();
		try {
			return fired;
		}finally {
			genLock.unlock();
		}
	}
	
	public void setAlive(boolean alive) {
		genLock.lock();
		try {
			isAlive = alive;
		}finally {
			genLock.unlock();
		}
	}
	
	public boolean getAlive() {
		genLock.lock();
		try {
			return isAlive;
		}finally {
			genLock.unlock();
		}
	}
	
	public void setPosX(int x) {
		genLock.lock();
		try {
			this.x = x;
		}finally {
			genLock.unlock();
		}
	}
	
	public int getPosX() {
		genLock.lock();
		try {
			return x;
		}finally {
			genLock.unlock();
		}
	}
	
	public void setPosY(int y) {
		genLock.lock();
		try {
			this.y = y;
		}finally {
			genLock.unlock();
		}
	}
	
	public int getPosY() {
		genLock.lock();
		try {
			return y;
		}finally {
			genLock.unlock();
		}
	}

}
