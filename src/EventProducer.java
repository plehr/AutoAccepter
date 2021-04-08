import java.io.IOException;
import java.time.Duration;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventProducer extends Thread {
	private final String name = "EventProducer";
	private boolean running = true;

	public EventProducer() {

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

	private void work() throws InterruptedException {
		Main.sem.acquire();
		System.out.println("EventProducer WOKE UP");
		while (running)
			try {
				
				if (listen()) {
					Main.sem.release();
					Main.sem.acquire();
					System.out.println("EventProducer WOKE UP");
				}

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

	private boolean listen() throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(Duration.ofHours(2))
				  .build();
				Request request = new Request.Builder()
				  .url("http://" + System.getenv("HOST") + ":" + System.getenv("PORT")
					+ "/rest/events?timeout=300&events=PendingDevicesChanged")
				  .method("GET", null)
				  .addHeader("X-API-Key", System.getenv("API-KEY"))
				  .build();
				Response response = client.newCall(request).execute();
		
		
//		OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS)
//				.connectTimeout(30, TimeUnit.SECONDS).build();
//		Request request = new Request.Builder()
//				.url("http://" + System.getenv("HOST") + ":" + System.getenv("PORT")
//						+ "/rest/events?timeout=300&events=PendingDevicesChanged")
//				.method("GET", null).addHeader("X-API-Key", System.getenv("API-KEY")).build();
//		Response response = client.newCall(request).execute();
		System.out.println("LISTENER " + response.code());
		int i = response.code();
		if (i == 200)
			return true;
		return false;
	}
}
