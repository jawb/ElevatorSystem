import java.util.ArrayList;
import java.lang.Thread;


public class Elevator {
	
	public Environment env;
	public ArrayList<CarButton> buttons;
	public ArrayList<Person> people;
	public Thread mainThread;
	public boolean ready = false;
	public volatile boolean running = false;
	public volatile boolean notPaused = true;
	public final Object p2eLock = new Object();
	public final Object d2eLock = new Object();
	
	public Elevator(Environment env) {
		this.env = env;
		people = new ArrayList<Person>();
		buttons = new ArrayList<CarButton>();
		mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (notPaused) {
					while (running) {
						step();
						try { Thread.sleep(100); } catch(Exception ex) {ex.printStackTrace();}
					}
				}
			}
		});
	}
	
	public boolean canLift() {
		return true;
	}
	
	public boolean canGo() {
		return true;
	}
	
	public void step() {
		
	}
	
	public void enter(int floor, Person p) {
		people.add(p);
		env.floors.get(floor).people.remove(p);
	}
	
	public void leave(Person p) {
		people.remove(p);
	}
	
	public void leave(int floor) {
		ArrayList<Person> leaving = new ArrayList<Person>(); 
		for (Person p : people) {
			if (p.floor == floor) leaving.add(p);
		}
		for (Person p : leaving) {
			leave(p);
		}
		leaving.clear();
	}
	
	public void beReady() {
		inFloor = true;
		currentFloor = env.floors.get(0);
		z = currentFloor.position;
		for (Floor f : env.floors) {
			buttons.add(new CarButton(this, f.floor));
		}
		stop();
		speed = 10;
		ready = true;
		mainThread.start();
	}

}
