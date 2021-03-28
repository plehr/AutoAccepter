import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Event;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventProducer extends Thread {
	private final String name = "EventProducer";
	private boolean running = true;
	private final LinkedBlockingQueue<Event> evList;

	public EventProducer(LinkedBlockingQueue<Event> l) {
		this.evList = l;
	}

	@Override
	public void run() {
		while (running) {
			try {
				work();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Helper.logErr(name, e);
			}
		}
	}

	private void work() {
		while (running)
			try {
				for (Event ev : getEvent())
					evList.offer(ev);
			} catch (Exception e) {
				Helper.logErr(name, e);
			}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	public void shutdown() {
		running = false;
	}

	private Event[] getEvent() throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS)
				.connectTimeout(30, TimeUnit.SECONDS).build();
		Request request = new Request.Builder()
				.url("http://" + System.getenv("HOST") + ":" + System.getenv("PORT") + "/rest/events?timeout=300&events=PendingDevicesChanged") 
				.method("GET", null).addHeader("X-API-Key", System.getenv("API-KEY")).build();
		Response response = client.newCall(request).execute();
		String str = response.body().string();
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Event.class, new CustomDeserializer());
		Gson gson = builder.create();
		Event[] result2 = gson.fromJson(str, Event[].class);
		return result2;
	}
}
