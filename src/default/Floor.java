import java.util.ArrayList; public   class  Floor {
	
	
	public ArrayList<Person> people;

	
	public HallButton up;

	
	public HallButton down;

	
	public int floor;

	
	
	public Floor  (Elevator elevator, int i) {
		floor = i;
		up = new HallButton(elevator, i);
		down = new HallButton(elevator, i);
		people = new ArrayList<Person>();
	
		up.setOnPressHandler(new OnPressHandler(up) {
			@Override
			public void onPress() {
				button.elevator.addHallCall(button.data, Elevator.UP);
			}
		});
		
		down.setOnPressHandler(new OnPressHandler(down) {
			@Override
			public void onPress() {
				button.elevator.addHallCall(button.data, Elevator.DOWN);
			}
		});
	}

	
	
	public float position;

	
	
	public Floor(Elevator elevator, int i, float position) {
		this(elevator, i);
		this.position = position;
	}


}
