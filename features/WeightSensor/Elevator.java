public class Elevator {
	
	public float weight;
	
	public void enter(int floor, Person p) {
		original(floor, p);
		weight += p.weight;
		System.out.println("Weight: "+weight);
	}
	
	public void leave(Person p) {
		original(p);
		weight -= p.weight;
		System.out.println("Weight: "+weight);
	}
	
}