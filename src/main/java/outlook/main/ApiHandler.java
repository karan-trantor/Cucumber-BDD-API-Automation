package outlook.main;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import com.jayway.jsonpath.JsonPath;

import cucumber.api.cli.Main;
import junit.framework.AssertionFailedError;

public class ApiHandler {

	public static String getTimeLineItems(String type, String startIndex, String categories, String limit,
			String userId, String searchStr, String status) {
		String finalResponse = null;
		try {

			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(
					Utils.readConfig("api_path"));
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(Utils.readConfig("admin_username"), Utils.readConfig("admin_password"));
			post.addHeader(new BasicScheme().authenticate(creds, post, null));

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("type", type));
			urlParameters.add(new BasicNameValuePair("startIndex", startIndex));
			urlParameters.add(new BasicNameValuePair("categories", categories));
			urlParameters.add(new BasicNameValuePair("limit", limit));
			urlParameters.add(new BasicNameValuePair("userId", userId));
			urlParameters.add(new BasicNameValuePair("searchStr", searchStr));
			urlParameters.add(new BasicNameValuePair("status", status));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			CloseableHttpResponse response = client.execute(post);
			System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				finalResponse = EntityUtils.toString(entity);

			} else {
				System.out.println("ENTITY IS NULL");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalResponse;

	}

	public static boolean verifyJsonElementPresentInTimeLineItems(String jsonResponse, String key, String value) {
		String responseFromJsonBasedUponKey = JsonPath.read(jsonResponse, "$..[*]" + key).toString();
		if (!responseFromJsonBasedUponKey.contains(value)) {
			System.out.println("****\n"+jsonResponse+"\n***");
			System.out.println("FAIL KEY: " + key + " VALUE:" + value +" BUT ACTUAL VALUE IS: "+responseFromJsonBasedUponKey);
			Assert.fail("FAIL KEY: " + key + " VALUE:" + value +" BUT ACTUAL VALUE IS: "+responseFromJsonBasedUponKey);
			return false;
		}
		return true;
	}

	public static void main(String[] args) {

	}
}
