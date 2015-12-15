package name.herve.jtlm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import name.herve.jtlm.model.TwitterUserCollection;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TestjTLM {

	private static void grantAccess() throws TwitterException, IOException {
		// The factory instance is re-useable and thread safe.
		Twitter twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer("sa7OXEYRd0sErmFqFiwrO77iO", "3UxdzRJLFrczysQeo4OzubMYbiJzAtBvQgfpjT4l0vMC34UC1X");
		RequestToken requestToken = twitter.getOAuthRequestToken();
		AccessToken accessToken = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (null == accessToken) {
			System.out.println("Open the following URL and grant access to your account:");
			System.out.println(requestToken.getAuthorizationURL());
			System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
			String pin = br.readLine();
			try {
				if (pin.length() > 0) {
					accessToken = twitter.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = twitter.getOAuthAccessToken();
				}
			} catch (TwitterException te) {
				if (401 == te.getStatusCode()) {
					System.out.println("Unable to get the access token.");
				} else {
					te.printStackTrace();
				}
			}
		}
		System.out.println("storeAccessToken[token] : " + accessToken.getToken());
		System.out.println("storeAccessToken[secret] : " + accessToken.getTokenSecret());
		System.exit(0);
	}

	public static void main(String args[]) throws Exception {
		grantAccess();
		
		TwitterWrapper tw = new TwitterWrapper();
		tw.init();
		tw.start();

		System.out.println("Ready");

		TwitterUserCollection friends = tw.getFriends(tw.getAuthenticatedUser().getScreenName());
		System.out.println("size:" + friends.getSize() + ", members:" + friends.getMembersSize());

		// for (TwitterList list : tw.getLists()) {
		// System.out.println("id:" + list.getId() + ", name:" + list.getName()
		// + ", description:" + list.getDescription());
		// tw.fillListMembers(list);
		// System.out.println("size:" + list.getSize() + ", members:" +
		// list.getMembersSize());
		// System.out.println();
		// }
		tw.stop();
	}
}