import java.io.*;
import sun.audio.*;


public class Elevator {
	
	public static final int MAX_WEIGHT = 700;
	public AudioPlayer alarm;
	public AudioStream as;
	public boolean LBPOkToGo = true;
	public boolean blockEnter = false;

	
	public Elevator(Environment env) {
		try {
			alarm = AudioPlayer.player;
            as = new AudioStream(new FileInputStream("res/alarm.wav"));
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public boolean canGo() {
		return original() && LBPOkToGo;
	}
	
	public boolean canLift() {
		return original() && !blockEnter;
	}
	
	public void enter(int floor, Person p) {
		original(floor, p);
		if (weight > MAX_WEIGHT) {
			System.out.println("Stop [LoadWeigthByPass]");
			LBPOkToGo = false;
			blockEnter = true;
			alarm.start(as);
		}
	}
	
	public void leave(int floor) {
		original(floor);
		if (weight <= MAX_WEIGHT) {
			alarm.stop(as);
			System.out.println("Start [LoadWeigthByPass]");
			LBPOkToGo = true;
			blockEnter = false;
		}
	}
	
	public void leave(Person p) {
		original(p);
		if (weight <= MAX_WEIGHT) {
			alarm.stop(as);
			System.out.println("Start [LoadWeigthByPass]");
			LBPOkToGo = true;
			blockEnter = false;
		}
	}
}
