public  class  Button {
	
	
	public int data;

	
	public Elevator elevator;

	
	public OnPressHandler handler;

	
	
	
	public Button(Elevator elevator, int data) {
		this.elevator = elevator;
		this.data = data;
	}

	
	
	public void setOnPressHandler(OnPressHandler handler) {
		this.handler = handler;
	}

	
	
	public void press() {
		handler.onPress();
	}


}
