import java.util.ArrayList;


public class Floor {
	
	private Button cancel;
	
	public Floor(Elevator elevator, int i) {
		cancel = new HallButton(elevator, i);
		cancel.setOnPressHandler(new OnPressHandler(cancel) {
			@Override
			public void onPress() {
				button.elevator.cancelCarCall(button.data);
			}
		});
	}
	
}
