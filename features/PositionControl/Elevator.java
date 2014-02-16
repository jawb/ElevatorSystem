public class Elevator {
	
	public Thread moveThread;
	
	
	public Elevator(Environment env) {
		moveThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (notPaused) {
					while (running) {
						isOkToGo();
						if (started) {
							z += -direction;
						}
						try { Thread.sleep(speed); } catch(Exception ex) {ex.printStackTrace();}
					}
				}
			}
		});
	}
	
	public void isOkToGo() {
		if (canGo()) start();
		else stop();
	}
	
	public void beReady() {
		original();
		moveThread.start();
	}
	
}
