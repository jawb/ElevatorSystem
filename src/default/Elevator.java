import java.util.ArrayList; 
import java.lang.Thread; 
import java.lang.Runnable; import java.io.*; 
import sun.audio.*; 

public   class  Elevator {
	
	
	public Environment env;

	
	public ArrayList<CarButton> buttons;

	
	public ArrayList<Person> people;

	
	public Thread mainThread;

	
	public boolean ready = false;

	
	public volatile boolean running = false;

	
	public volatile boolean notPaused = true;

	
	public final Object p2eLock = new Object();

	
	public final Object d2eLock = new Object();

	
	
	
	public Elevator  (Environment env) {
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
	
		carCalls = new ArrayList<Integer>();
	
		hallCalls = new ArrayList<HallCall>();
	
		try {
			alarm = AudioPlayer.player;
            as = new AudioStream(new FileInputStream("res/alarm.wav"));
		} catch (Exception e) { e.printStackTrace(); }
	
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

	
	
	 private boolean  canLift__wrappee__RegistredFloorStop  () {
		return true;
	}

	
	
	public boolean canLift() {
		return canLift__wrappee__RegistredFloorStop() && !blockEnter;
	}

	
	
	 private boolean  canGo__wrappee__WeightSensor  () {
		return true;
	}

	
	
	 private boolean  canGo__wrappee__SpeedControl  () {
		return canGo__wrappee__WeightSensor() && PSOkToGo;
	}

	
	
	 private boolean  canGo__wrappee__RegistredFloorStop  () {
		return canGo__wrappee__SpeedControl() && RFokToGo;
	}

	
	
	 private boolean  canGo__wrappee__PositionControl  () {
		return canGo__wrappee__RegistredFloorStop() && LBPOkToGo;
	}

	
	
	public boolean canGo() {
		return canGo__wrappee__PositionControl() && NoCallsOkToGo;
	}

	
	
	 private void  step__wrappee__Elevator  () {
		
	}

	

	
	 private void  step__wrappee__SpeedControl  () {
		if (inFloor) {
			if (currentFloor.floor == Main.FLOORS-1) {
				direction = DOWN;
				System.out.println("Hit the roof, flip direction: "+direction);
			}
			else if (currentFloor.floor == 0) {
				direction = UP;
				System.out.println("Hit the floor, flip direction: "+direction);
			}
			if (noCallsAhead()) {
				direction *= -1;
				System.out.println("No calls ahead, flip direction: "+direction);
				stop();
			}
		}
		step__wrappee__Elevator();
	}

	
	
	 private void  step__wrappee__PositionControl  () {
		step__wrappee__SpeedControl();
		if (inFloor) {
			enterLeaveFloor();
		}
		synchronized (p2eLock) { p2eLock.notify(); }
	}

	
	
	public void step() {
		step__wrappee__PositionControl();
		if (carCalls.isEmpty() && hallCalls.isEmpty()) {
//			System.out.println("Stop, just rest [StopControl]");
			NoCallsOkToGo = false;
		}
		else {
//			System.out.println("Start [StopControl]");
			NoCallsOkToGo = true;
		}
	}

	
	
	 private void  enter__wrappee__CarButton  (int floor, Person p) {
		people.add(p);
		env.floors.get(floor).people.remove(p);
	}

	
	
	 private void  enter__wrappee__CarCallRegistration  (int floor, Person p) {
		enter__wrappee__CarButton(floor, p);
		weight += p.weight;
		System.out.println("Weight: "+weight);
	}

	
	
	 private void  enter__wrappee__RegistredFloorStop  (int floor, Person p) {
		enter__wrappee__CarCallRegistration(floor, p);
		ArrayList<HallCall> toDel = new ArrayList<HallCall>();
		for (HallCall hc : hallCalls) {
			if (hc.floor == floor && hc.direction == direction) toDel.add(hc);
		}
		for (HallCall hc : toDel) {
			hallCalls.remove(hc);
		}
		toDel.clear();
	}

	
	
	public void enter(int floor, Person p) {
		enter__wrappee__RegistredFloorStop(floor, p);
		if (weight > MAX_WEIGHT) {
			System.out.println("Stop [LoadWeigthByPass]");
			LBPOkToGo = false;
			blockEnter = true;
			alarm.start(as);
		}
	}

	
	
	 private void  leave__wrappee__CarButton  (Person p) {
		people.remove(p);
	}

	
	
	 private void  leave__wrappee__RegistredFloorStop  (Person p) {
		leave__wrappee__CarButton(p);
		weight -= p.weight;
		System.out.println("Weight: "+weight);
	}

	
	
	public void leave(Person p) {
		leave__wrappee__RegistredFloorStop(p);
		if (weight <= MAX_WEIGHT) {
			alarm.stop(as);
			System.out.println("Start [LoadWeigthByPass]");
			LBPOkToGo = true;
			blockEnter = false;
		}
	}

	
	
	 private void  leave__wrappee__RegistredFloorStop  (int floor) {
		ArrayList<Person> leaving = new ArrayList<Person>(); 
		for (Person p : people) {
			if (p.floor == floor) leaving.add(p);
		}
		for (Person p : leaving) {
			leave(p);
		}
		leaving.clear();
	}

	
	
	public void leave(int floor) {
		leave__wrappee__RegistredFloorStop(floor);
		if (weight <= MAX_WEIGHT) {
			alarm.stop(as);
			System.out.println("Start [LoadWeigthByPass]");
			LBPOkToGo = true;
			blockEnter = false;
		}
	}

	
	
	 private void  beReady__wrappee__WeightSensor  () {
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

	
	
	
	 private void  beReady__wrappee__LoadWeighingByPass  () {
		beReady__wrappee__WeightSensor();
		positionThread.start();
	}

	
	
	public void beReady() {
		beReady__wrappee__LoadWeighingByPass();
		moveThread.start();
	}

	
	
	public static final int UP = 1;

	
	public static final int DOWN = -1;

	
	public int direction = UP;

	
	
	public boolean noCallsAhead() {
		if (direction == UP) {
			for (HallCall hc : hallCalls) {
				if (hc.floor >= currentFloor.floor) return false;
			}
			for (int cc : carCalls) {
				if (cc >= currentFloor.floor) return false;
			}
		}
		else if (direction == DOWN) {
			for (HallCall hc : hallCalls) {
				if (hc.floor <= currentFloor.floor) return false;
			}
			for (int cc : carCalls) {
				if (cc <= currentFloor.floor) return false;
			}
		}
		return true;
	}

	
	
	public int getDir(int at, int to) {
		return (to-at > 0)?UP:DOWN;
	}

	
	
	public float weight;

	
	
	public float z;

	
	public boolean inFloor = false;

	
	public boolean pInFloor = false;

	
	public Floor currentFloor;

	
	public Thread positionThread;

	
	public boolean PSOkToGo = true;

	
	
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

	

	
	public void closeDoor() {
		doorStatus = CLOSING;
	}

	
	
	public void openDoor() {
		doorStatus = OPENING;
	}

	
	
	public ArrayList<Integer> carCalls;

	
	
	public void addCarCall(int floor) {
		if (carCalls.contains(floor)) return;
		carCalls.add(floor);
	}

	
	
	public ArrayList<HallCall> hallCalls;

	
	
	public void addHallCall(int floor, int dir) {
		hallCalls.add(new HallCall(floor, dir));
	}

	
	
	public void start() {
		this.started = true;
	}

	
	
	private int speed;

	
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	
	
	public boolean RFokToGo = true;

	

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

	
	
	public static final int MAX_WEIGHT = 700;

	
	public AudioPlayer alarm;

	
	public AudioStream as;

	
	public boolean LBPOkToGo = true;

	
	public boolean blockEnter = false;

	
	
	public Thread moveThread;

	
	
	public void isOkToGo() {
		if (canGo()) start();
		else stop();
	}

	
	
	public boolean started;

	
	public boolean NoCallsOkToGo = false;

	
	
	public void stop() {
		this.started = false;
	}


}
