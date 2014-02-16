public class Elevator {
	
	public boolean started;
	public boolean NoCallsOkToGo = false;
	
	public void stop() {
		this.started = false;
	}
	
	public boolean canGo() {
		return original() && NoCallsOkToGo;
	}
	
	public void step() {
		original();
		if (carCalls.isEmpty() && hallCalls.isEmpty()) {
//			System.out.println("Stop, just rest [StopControl]");
			NoCallsOkToGo = false;
		}
		else {
//			System.out.println("Start [StopControl]");
			NoCallsOkToGo = true;
		}
	}
	
}
