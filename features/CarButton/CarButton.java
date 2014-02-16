public class CarButton extends Button {
	
	public CarButton(Elevator elevator, int i) {
		super(elevator, i);
		final CarButton that = this;
		setOnPressHandler(new OnPressHandler(that) {
			@Override
			public void onPress() {
				button.elevator.addCarCall(button.data);
			}
		});
	}

}