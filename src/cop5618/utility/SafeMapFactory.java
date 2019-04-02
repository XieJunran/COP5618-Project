package cop5618.utility;

public class SafeMapFactory {

	private static final int[][] DEFAULT_MAP = {
			{ 0,  0,  0, -1,  0,  0, -1, -1,  0,  0,  0, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 0
			{ 0,  0,  0, -1,  0,  0, -1, -1,  0,  0,  0, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 1
			{ 0,  0,  0, -1,  0,  0, -1, -1,  0,  0,  0, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 2	
			{ 0, -1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 3
			{ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -3, -3,  0,  0,  0,  0},	// 4	
			{ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -3, -3, -3,  0,  0,  0,  0},	// 5
			{ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1, -1,  0,  0, -3, -3,  0,  0,  0,  0,  0},	// 6	
			{-2, -2, -2,  0,  0,  0,  0, -3, -3,  0,  0,  0,  0,  0,  0,  0,  0, -3, -3,  0,  0,  0,  0,  0},	// 7
			{ 0,  0,  0,  0,  0,  0,  0, -3, -3,  0,  0,  0,  0,  0,  0,  0,  0, -1, -3, -3, -3, -1, -1, -1},	// 8	
			{ 0,  0,  0,  0,  0, -3, -3, -3, -3,  0,  0,  0,  0,  0, -1, -1,  0, -1,  0, -3,  0,  0,  0,  0},	// 9
			{-1, -1,  0,  0, -3, -3, -3, -3, -3, -3,  0,  0, -2, -2, -2, -1, -1, -1,  0,  0,  0,  0,  0,  0},	// 10	
			{-1, -1,  0,  0, -3, -3, -3, -3, -3, -3, -3,  0, -2, -2, -2,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 11
			{ 0,  0,  0, -3, -3, -3, -3, -3, -3, -3,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -2, -2, -2},	// 12	
			{ 0,  0,  0,  0, -3, -3, -3, -3, -3, -3,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 13
			{-1, -1,  0, -3, -3, -3, -3, -3, -3, -3, -3, -2,  0,  0,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0},	// 14	
			{-1, -1,  0,  0,  0,  0,  0, -3, -3, -3, -3, -2,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 15
			{ 0,  0,  0,  0,  0,  0,  0,  0, -3, -3, -3, -2,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 16	
			{ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -2,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 17
			{-2, -2, -2,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},	// 18	
			{ 0,  0,  0,  0,  0,  0,  0, -1, -1,  0,  0, -1, -1, -2,  0,  0,  0,  0,  0, -2, -2, -2,  0,  0},	// 19
			{ 0,  0,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0,  0, -2,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0},	// 20
			{ 0,  0,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0,  0, -2,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0},	// 21
			{ 0,  0,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0,  0, -2,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0},	// 22	
			{ 0,  0,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0,  0, -2,  0,  0,  0,  0,  0, -1, -1,  0,  0,  0}	// 23
	};
	
	final int[][] copyOfDefaultMap= new int[BattleField.BF_SIZE][BattleField.BF_SIZE];
	
	private SafeMapFactory() {
		for(int i = 0; i <  BattleField.BF_SIZE; i++) {
			for(int j = 0; j <  BattleField.BF_SIZE; j++) {
				copyOfDefaultMap[i][j] = DEFAULT_MAP[i][j];
			}
		}
	}
	
	public static int[][] newMapInstance() {
		SafeMapFactory safeMap = new SafeMapFactory();
		return safeMap.copyOfDefaultMap;
	}
	
}