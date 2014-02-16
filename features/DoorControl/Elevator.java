import java.util.ArrayList;
import java.lang.Runnable;


public class Elevator {
	
	public volatile int leftDoorPos;
	public volatile int rightDoorPos;
	public volatile int doorStep = 0;
	public final static int OPENED  = 0;
	public final static int OPENING = 1;
	public final static int CLOSING = 2;
	public final static int CLOSED  = 3;
	public int doorStatus = CLOSED;
	public final static int NUM_STEP = 16;
	public final static int STEP  = Main.ELEVATOR_WIDTH/(2*NUM_STEP);
	Thread doorThread;
	
	public Elevator(Environment env) {
		doorThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (notPaused) {
					while (running) {
						if (doorStatus == CLOSING) {
							if (doorStep>0) {
			            		leftDoorPos += STEP;
			            		rightDoorPos -= STEP;
			            		--doorStep;
			            	}
					    	else {
					    		doorStatus = CLOSED;
					    		synchronized (d2eLock) { d2eLock.notify(); }
					    	}
						}
						if (doorStatus == OPENING) {
							if (doorStep<NUM_STEP) {
			            		leftDoorPos -= STEP;
			            		rightDoorPos += STEP;
			            		++doorStep;
			            	}
					    	else {
					    		doorStatus = CLOSED;
					    		synchronized (d2eLock) { d2eLock.notify(); }
					    	}
						}
						try { Thread.sleep(100); } catch(Exception ex) {ex.printStackTrace();}
					}
				}
			}
		});
		doorThread.start();
	}

	
	public void closeDoor() {
		doorStatus = CLOSING;
	}
	
	public void openDoor() {
		doorStatus = OPENING;
	}
	
}