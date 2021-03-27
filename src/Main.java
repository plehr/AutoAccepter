import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Event;

public class Main {

	public static void main(String[] args) {
		List<Event> eventList = Collections.synchronizedList(new ArrayList<Event>());

		EventProducer evProd = new EventProducer(eventList);
		evProd.start();
		System.out.println("Please type enter to stop process:");
		try {
			System.in.read();
		} catch (IOException e) {
		}
		System.out.println("SIZE: " + eventList.size());
		for (Event event : eventList)
			System.out.println(event.getClass());

		evProd.shutdown();
	}
}
