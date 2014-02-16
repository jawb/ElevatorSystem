public class Elevator {
	
	public static final int UP = 1;
	public static final int DOWN = -1;
	public int direction = UP;

	
	public void step() {
		if (inFloor) {
			if (currentFloor.floor == Main.FLOORS-1) {
				direction = DOWN;
				System.out.println("Hit the roof, flip direction: "+direction);
			}
			else if (currentFloor.floor == 0) {
				direction = UP;
				System.out.println("Hit the floor, flip direction: "+direction);
			}
			if (noCallsAhead()) {
				direction *= -1;
				System.out.println("No calls ahead, flip direction: "+direction);
				stop();
			}
		}
		original();
	}
	
	public boolean noCallsAhead() {
		if (direction == UP) {
			for (HallCall hc : hallCalls) {
				if (hc.floor >= currentFloor.floor) return false;
			}
			for (int cc : carCalls) {
				if (cc >= currentFloor.floor) return false;
			}
		}
		else if (direction == DOWN) {
			for (HallCall hc : hallCalls) {
				if (hc.floor <= currentFloor.floor) return false;
			}
			for (int cc : carCalls) {
				if (cc <= currentFloor.floor) return false;
			}
		}
		return true;
	}
	
	public int getDir(int at, int to) {
		return (to-at > 0)?UP:DOWN;
	}
}
