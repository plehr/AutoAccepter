import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.omg.CORBA.Environment;

import com.google.gson.JsonParser;

import events.notImplementedEvent;
import okhttp3.*;
import model.Event;

public class Main {
	JsonParser p = new JSONParser();

	public static void main(String[] args) {
		boolean running = true;
		Queue<Event> eq = new LinkedList<Event>();
		Main m = new Main();
		while (running) {
			try {
				for (Event ev : m.getEvent()) {
					eq.add(ev);
				}
			} catch (Exception e) {
				System.out.println("RUNNING ->" + e.getMessage());
				running = false;
			}
		}
		System.out.println("AUSGAB");
		for (Event event : eq) {
			System.out.println(event.id);
			if (event instanceof notImplementedEvent)
				System.out.println(event.getClass());
			System.out.println(event.getClass());
		}
	}

	private Event[] getEvent() throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS)
				.connectTimeout(30, TimeUnit.SECONDS).build();
		Request request = new Request.Builder().url("http://" + System.getenv("HOST") + ":"+ System.getenv("PORT") +"/rest/events").method("GET", null)// ?timeout=300&events=PendingDevicesChanged
				.addHeader("X-API-Key", System.getenv("API-KEY")).build();
		Response response = client.newCall(request).execute();
		String str = response.body().string();
		while (str.contains(" "))
			str = str.replace(" ", "");
		str = str.substring(1, str.length() - 1);
		System.out.println(str);

		ArrayList<Event> arle = new ArrayList<Event>();

		StringTokenizer strt = new StringTokenizer(str, "},{");
		while (strt.hasMoreTokens())
			arle.add(parseStringToEvent(strt.nextToken() ));

		return (Event[]) arle.toArray();
	}

	private Event parseStringToEvent(String s) {
		Event e;
		try {
			e = (Event) p.parse(s);
		} catch (ParseException e1) {
			e = new notImplementedEvent();
		}
		return e;
	}
}
