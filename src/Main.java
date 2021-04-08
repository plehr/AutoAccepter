import java.io.IOException;
import java.util.concurrent.Semaphore;

public class Main {
	public static String KEY_CONFIG = "hinnp-engki";
	public static String KEY_BACKUP = "qa5r7-ya2sz";

	public static Semaphore sem = new Semaphore(1);

	public static void main(String[] args) throws InterruptedException, IOException {
		

//		String s = Helper.getFolderConfig(KEY_CONFIG);
//		String id = "JBCNZXB-55KXM32-I7ML7D7-6SNI2AN-IYPZ62X-LPGXPMJ-HMTP3AM-6R5S5AT";
//		
//		String folderConfigShare = s.replace("]", ",{\"deviceID\": \"" + id
//		+ "\",\"introducedBy\": \"\",\"encryptionPassword\": \"\"}]");
//		
//		Helper.setFolderConfig(KEY_CONFIG, folderConfigShare);

		EventConsumer evCons = new EventConsumer();
		 evCons.start();

		EventProducer evProd = new EventProducer();
		evProd.start();
		

		System.out.println("Please type enter to stop process:");
		try {
			System.in.read();
		} catch (IOException e) {
			System.out.println("MAIN-IOEXC: " + e.getLocalizedMessage());
		}
		evProd.shutdown();
		evCons.shutdown();
	}
}
