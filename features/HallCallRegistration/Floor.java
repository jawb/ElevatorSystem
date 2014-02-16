public class Floor {
	
	public Floor(Elevator elevator, int i) {
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
}
