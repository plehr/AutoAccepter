
public class Helper {
	public static void logErr(String sender, Exception ex) {
		System.err.println("Error on " + sender + ": " + ex.getMessage());
	}
}
