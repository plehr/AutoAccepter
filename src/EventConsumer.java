import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.security.auth.login.CredentialException;

public class EventConsumer extends Thread {
	private final String name = "EventConsumer";
	boolean running = true;

	public EventConsumer() {
	}

	@Override
	public void run() {
		while (running)
			try {
				Main.sem.acquire();
				System.out.println("EventConsumer WOKE UP");
				work();
				Main.sem.release();
			} catch (Exception e) {
				Helper.logErr(name, e);
			}
	}

	private void work() throws InterruptedException, CredentialException {
		try {
			HashMap<String, String> m = Helper.getPendingDevices();
			Iterator<Entry<String, String>> it = m.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Entry<String, String>) it.next();
				System.out.println(entry.getKey() + " -> " + entry.getValue());
				Helper.addToList(entry.getKey(), entry.getValue());
				String folderConfigShare = Helper.getFolderConfig(Main.KEY_CONFIG);
				String folderBackupShare = Helper.getFolderConfig(Main.KEY_BACKUP);
				folderConfigShare = folderConfigShare.replace("]", ",{\"deviceID\": \"" + entry.getKey()
						+ "\",\"introducedBy\": \"\",\"encryptionPassword\": \"\"}]");
				folderBackupShare = folderBackupShare.replace("]", ",{\"deviceID\": \"" + entry.getKey()
						+ "\",\"introducedBy\": \"\",\"encryptionPassword\": \"\"}]");
				Helper.setFolderConfig(Main.KEY_CONFIG, folderConfigShare);
				Helper.setFolderConfig(Main.KEY_BACKUP, folderBackupShare);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void shutdown() {
		running = false;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}
}
