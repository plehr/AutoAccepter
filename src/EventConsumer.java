import java.util.concurrent.LinkedBlockingQueue;

import events.NotImplementedEvent;
import model.Event;

public class EventConsumer extends Thread {
	private final String name = "EventConsumer";
	boolean running = true;
	private final LinkedBlockingQueue<Event> evList;

	public EventConsumer(LinkedBlockingQueue<Event> l) {
		evList = l;
	}

	@Override
	public void run() {
		while (running) {
			try {
				work();
			} catch (Exception e) {
				Helper.logErr(name, e);
			}
		}
	}

	private void work() throws InterruptedException {
		Event ev = evList.take();
		System.out.println(ev.getClass());
		if (ev.getClass() == NotImplementedEvent.class)
			System.out.println("NotImplement: " + ev.globalID);
	}

	public void shutdown() {
		running = false;
		evList.add(new NotImplementedEvent());
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}
}
