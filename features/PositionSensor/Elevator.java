public class Elevator {
	
	public float z;
	public boolean inFloor = false;
	public boolean pInFloor = false;
	public Floor currentFloor;
	public Thread positionThread;
	public boolean PSOkToGo = true;
	
	
	public Elevator(Environment env) {
		positionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (notPaused) {
					while (running) {
						check();
						if (!pInFloor && inFloor) inFloorFirstTime();
						try { Thread.sleep(speed/3); } catch(Exception ex) {ex.printStackTrace();}
					}
				}
			}
		});
	}
	
	public void check() {
		pInFloor = inFloor;
		for (Floor floor : env.floors) {
			if (z == floor.position) {
				inFloor = true;
				currentFloor = floor;
				return;
			}
		}
		inFloor = false;
		currentFloor = null;
	}
	
	
	public void beReady() {
		original();
		positionThread.start();
	}
	
	public void inFloorFirstTime() {
		System.out.println("In Floor 1st time");
		System.out.println("Stop to check  [PositionSensor]");
		PSOkToGo = false;
		synchronized (p2eLock) {
			try { p2eLock.wait(); } catch(Exception ex) {ex.printStackTrace();}
		}
		System.out.println("Start [PositionSensor]");
		PSOkToGo = true;
	}
	
	public boolean canGo() {
		return original() && PSOkToGo;
	}
	
}