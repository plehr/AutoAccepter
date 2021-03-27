import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import events.DeviceRejected;
import events.PendingDevicesChanged;
import events.NotImplementedEvent;
import model.Event;

public class CustomDeserializer implements JsonDeserializer<Event> {

	@Override
	public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jObject = (JsonObject) json;
		JsonElement typeObj = jObject.get("type");

		if (typeObj != null) {
			String typeVal = typeObj.getAsString();

			switch (typeVal) {
			case "PendingDevicesChanged":
				return context.deserialize(json, PendingDevicesChanged.class);
			case "DeviceRejected":
				return context.deserialize(json, DeviceRejected.class);
			default:
				return context.deserialize(json, NotImplementedEvent.class);
			}
		}
		return null;
	}
}