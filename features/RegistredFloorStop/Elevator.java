import java.util.ArrayList;


public class Elevator {
	
	public boolean RFokToGo = true;
	
	public boolean canGo() {
		return original() && RFokToGo;
	}
	
	public void step() {
		original();
		if (inFloor) {
			enterLeaveFloor();
		}
		synchronized (p2eLock) { p2eLock.notify(); }
	}

	public void enterLeaveFloor() {
		ArrayList<Person> ppl = awaitingAt(currentFloor);
		boolean thereIscarCalls = carCalls.contains(currentFloor.floor);
		boolean thereIsHallCalls = !ppl.isEmpty();
		if(canLift()) if (thereIscarCalls || thereIsHallCalls) {
			System.out.println("Stop [RegistredFloorStop]");
			RFokToGo = false;
			openDoor();
			System.out.println("Wait to open the door");
			synchronized (d2eLock) { try { d2eLock.wait(); } catch(Exception ex) {ex.printStackTrace();}}
			System.out.println("Door opened");
			if (thereIscarCalls) {
				System.out.println("Person willing to leave at "+currentFloor.floor);
				leave(currentFloor.floor);
				carCalls.remove(new Integer(currentFloor.floor));
			}
			if (thereIsHallCalls) {
				for (Person p : ppl) {
					System.out.println("Person willing to enter from "+currentFloor.floor);
					enter(currentFloor.floor, p);
					buttons.get(p.floor).press();
				}
			}
			closeDoor();
			System.out.println("Wait to close the door");
			synchronized (d2eLock) { try { d2eLock.wait(); } catch(Exception ex) {ex.printStackTrace();}}
			System.out.println("Door closed");
			System.out.println("Start [RegistredFloorStop]");
			RFokToGo = true;
		}
	}
	
	public ArrayList<Person> awaitingAt(Floor f) {
		ArrayList<Person> ppl = new ArrayList<Person>();
		for (Person p : f.people) {
			if (this.direction == p.dir) ppl.add(p);
		}
		return ppl;
	}

}
