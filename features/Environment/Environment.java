import java.util.ArrayList;

public class Environment {
	
	public ArrayList<Floor> floors;

	
	public void addFloor(Floor floor) {
		floors.add(floor);
	}
	
	public Environment() {
		floors = new ArrayList<Floor>();
	}
	
}
