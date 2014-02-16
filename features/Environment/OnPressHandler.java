public abstract class OnPressHandler {
	
	public Button button;
	
	public OnPressHandler(Button button) {
		this.button = button;
	}
	
	abstract public void onPress();
}