import java.io.IOException;
import java.util.HashMap;
import javax.security.auth.login.CredentialException;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Helper {
	public static void logErr(String sender, Exception ex) {
		System.err.println("Error on " + sender + ": " + ex.getMessage());
	}

	public static HashMap<String, String> getPendingDevices() throws IOException, CredentialException {
		System.out.println("GET pendingDevices");
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder()
				.url("http://" + System.getenv("HOST") + ":" + System.getenv("PORT") + "/rest/cluster/pending/devices")
				.method("GET", null).addHeader("X-API-Key", System.getenv("API-KEY")).build();
		Response response = client.newCall(request).execute();
		String strResponse = response.body().string();

		if (response.code() != 200)
			throw new CredentialException();

		HashMap<String, String> m = new HashMap<>();

		JSONObject obj = new JSONObject(strResponse);
		for (String key : obj.keySet())
			m.put(key, obj.getJSONObject(key).get("name").toString());

		return m;
	}

	public static void addToList(String devID, String name) {
		try {
			System.out.println("Add device to list " + devID + "(" + name + ")");
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					" { \"deviceID\": \"" + devID + "\"," + "\"name\": \"" + name + "\","
							+ "\"addresses\": [\"dynamic\"]," + "\"compression\": \"metadata\"," + "\"certName\": \"\","
							+ "\"introducer\": false," + "\"skipIntroductionRemovals\": false,"
							+ "\"introducedBy\": \"\"," + "\"paused\": false," + "\"allowedNetworks\": [],"
							+ "\"autoAcceptFolders\": false," + "\"maxSendKbps\": 0," + "\"maxRecvKbps\": 0,"
							+ "\"ignoredFolders\": []," + "\"maxRequestKiB\": 0," + "\"untrusted\": false,"
							+ "\"remoteGUIPort\": 0" + "}");
			Request request = new Request.Builder()
					.url("http://" + System.getenv("HOST") + ":" + System.getenv("PORT") + "/rest/config/devices")
					.method("POST", body).addHeader("X-API-Key", System.getenv("API-KEY"))
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			//System.out.println("STATUS-Code " + response.code() + " Response" + response.body().string());
		} catch (Exception e) {
			System.out.println("ERROR addToList: " + e.getLocalizedMessage());
		}
	}

	public static String getFolderConfig(String folderID) {
		try {
			System.out.println("Get FolderConfig for " + folderID);
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			Request request = new Request.Builder()
					.url("http://" + System.getenv("HOST") + ":" + System.getenv("PORT") + "/rest/config/folders/"
							+ folderID)
					.method("GET", null).addHeader("X-API-Key", System.getenv("API-KEY"))
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (Exception e) {
			System.out.println("ERROR getFolderConfig: " + e.getLocalizedMessage());
		}
		return "";
	}

	public static void setFolderConfig(String folderID, String config) {
		try {
			System.out.println("Set FolderConfig for " + folderID);
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, config);
			Request request = new Request.Builder()
					.url("http://" + System.getenv("HOST") + ":" + System.getenv("PORT") + "/rest/config/folders/"
							+ folderID)
					.method("PUT", body).addHeader("X-API-Key", System.getenv("API-KEY"))
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			System.out.println("STATUS-Code " + response.code() + " Response" + response.body().string());
		} catch (IOException e) {
			System.out.println("ERROR setFolderConfig (" + folderID + "): " + e.getLocalizedMessage());
		}
	}
}
