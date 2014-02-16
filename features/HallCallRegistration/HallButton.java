public class HallButton {
	
	public OnPressHandler handler;
	
	public void setOnPressHandler(OnPressHandler handler) {
		this.handler = handler;
	}
	
	public void press() {
		handler.onPress();
	}
}