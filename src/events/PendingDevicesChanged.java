package events;

public class PendingDevicesChanged extends model.Event {
	model.Data[] data;

	@Override
	public String toString() {
		return "PendingDevicesChanged with id: " + globalID + " from " + data[0].deviceID + " (" + data[0].deviceID
				+ ")";
	}
}
