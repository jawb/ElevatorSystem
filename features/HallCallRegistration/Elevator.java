import java.util.ArrayList;


public class Elevator {
	
	public ArrayList<HallCall> hallCalls;
	
	public Elevator(Environment env) {
		hallCalls = new ArrayList<HallCall>();
	}
	
	public void addHallCall(int floor, int dir) {
		hallCalls.add(new HallCall(floor, dir));
	}
	
	public void enter(int floor, Person p) {
		original(floor, p);
		ArrayList<HallCall> toDel = new ArrayList<HallCall>();
		for (HallCall hc : hallCalls) {
			if (hc.floor == floor && hc.direction == direction) toDel.add(hc);
		}
		for (HallCall hc : toDel) {
			hallCalls.remove(hc);
		}
		toDel.clear();
	}

}
