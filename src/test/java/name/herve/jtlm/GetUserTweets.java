package name.herve.jtlm;

import java.io.File;

import name.herve.jtlm.model.Tweet;
import name.herve.jtlm.model.TwitterUser;
import name.herve.jtlm.persistence.SimpleStorage;

public class GetUserTweets {
	public final static String STORAGE_FILE = "D:/tmp/tweet_storage_2.bin";

	public static SimpleStorage getStorage() throws JTLMException {
		File f = new File(STORAGE_FILE);

		if (f.exists()) {
			try {
				return SimpleStorage.load(f);
			} catch (JTLMException e) {
				System.err.println("Unable to load the previous storage : " + e.getMessage());
				return SimpleStorage.newStorage();
			}
		} else {
			return SimpleStorage.newStorage();
		}
	}

	public static void main(String[] args) throws JTLMException {
		SimpleStorage storage = getStorage();

		String[] users = args;

		TwitterWrapper tw = new TwitterWrapper();
		tw.init();
		tw.start();
		System.out.println("Ready");

		for (String user : users) {
			for (Tweet t : tw.getTweets(user)) {
				storage.addTweet(user, t);
			}
			for (TwitterUser tu : tw.getFriends(user)) {
				storage.addFollowed(user, tu);
			}
			System.out.println(user + " : " + storage.getFolloweds(user).size());
		}

		tw.stop();

		SimpleStorage.save(storage, new File(STORAGE_FILE));
	}

}
