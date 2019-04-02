package cop5618.utility;

import cop5618.utility.Tank.Direction;
import cop5618.utility.Tank.Type;

public class SafeTankFactory {

	private final Tank tank;
	
	private SafeTankFactory(BattleField battleField, Type type, int x, int y) {
		tank = new Tank(battleField, type, x, y);
	}
	
	private SafeTankFactory(BattleField battleField, Type type, Direction direction, int x, int y) {
		tank = new Tank(battleField, type, direction, x, y);
	}
	
	public static Tank newTankInstance(BattleField battleField, Type type, int x, int y) {
		SafeTankFactory safeTank = new SafeTankFactory(battleField, type, x, y);
		return safeTank.tank;
	}
	
	public static Tank newTankInstance(BattleField battleField, Type type, Direction direction, int x, int y) {
		SafeTankFactory safeTank = new SafeTankFactory(battleField, type, direction, x, y);
		return safeTank.tank;
	}
	
}
