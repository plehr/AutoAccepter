import java.io.IOException;
import java.util.concurrent.*;
import model.Event;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		LinkedBlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>();

		EventProducer evProd = new EventProducer(eventQueue);
		evProd.start();

		EventConsumer evCons = new EventConsumer(eventQueue);
		evCons.start();

		System.out.println("Please type enter to stop process:");
		try {
			System.in.read();
		} catch (IOException e) {
		}

		evProd.shutdown();
		evCons.shutdown();
	}
}
