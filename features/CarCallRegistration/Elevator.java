public class Elevator {
	
	public void addCarCall(int floor) {
		if (carCalls.contains(floor)) return;
		carCalls.add(floor);
	}

}