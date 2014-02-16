import java.util.ArrayList;


public class Floor {
	
	public ArrayList<Person> people;
	public HallButton up;
	public HallButton down;
	public int floor;
	
	
	public Floor(Elevator elevator, int i) {
		floor = i;
		up = new HallButton(elevator, i);
		down = new HallButton(elevator, i);
		people = new ArrayList<Person>();
	}
}
