package events;

public class DeviceRejected extends model.Event {
	model.Data data;

	@Override
	public String toString() {
		return "DeviceRejected with id: " + globalID + " from " + data.deviceID + " (" + data.deviceID + ")";
	}
}
