public   class  HallButton  extends Button {
	
	
	public HallButton(Elevator elevator, int i) {
		super(elevator, i);
	}

	
	
	public OnPressHandler handler;

	
	
	public void setOnPressHandler(OnPressHandler handler) {
		this.handler = handler;
	}

	
	
	public void press() {
		handler.onPress();
	}


}
