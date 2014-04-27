package name.herve.jtlm.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import name.herve.jtlm.JTLMException;
import name.herve.jtlm.model.Tweet;
import name.herve.jtlm.model.TwitterUser;

public class SimpleStorage implements Serializable {
	private static final long serialVersionUID = 9000205661205378362L;

	public static SimpleStorage load(File f) throws JTLMException {
		try {
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			SimpleStorage storage = (SimpleStorage) ois.readObject();
			ois.close();
			return storage;
		} catch (ClassNotFoundException e) {
			throw new JTLMException(e);
		} catch (IOException e) {
			throw new JTLMException(e);
		}
	}

	public static SimpleStorage newStorage() throws JTLMException {
		SimpleStorage ss = new SimpleStorage();
		ss.init();
		return ss;
	}

	public static void save(SimpleStorage storage, File f) throws JTLMException {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(storage);
			oos.close();
		} catch (IOException e) {
			throw new JTLMException(e);
		}
	}

	private Map<String, Map<Long, Tweet>> tweets;
	private Map<String, Map<String, TwitterUser>> followeds;

	private SimpleStorage() {
		super();
	}

	public synchronized void addFollowed(String user, TwitterUser follower) {
		if (!followeds.containsKey(user)) {
			followeds.put(user, new HashMap<String, TwitterUser>());
		}
		followeds.get(user).put(follower.getScreenName(), follower);
	}

	public synchronized void addTweet(String user, Tweet tweet) {
		if (!tweets.containsKey(user)) {
			tweets.put(user, new HashMap<Long, Tweet>());
		}
		tweets.get(user).put(tweet.getId(), tweet);
	}

	public Map<String, Map<String, TwitterUser>> getFolloweds() {
		return followeds;
	}

	public Collection<TwitterUser> getFolloweds(String user) {
		if (!followeds.containsKey(user)) {
			return null;
		}

		return followeds.get(user).values();
	}

	public Collection<String> getStoredUsers() {
		return tweets.keySet();
	}

	public Collection<Tweet> getTweets(String user) {
		if (!tweets.containsKey(user)) {
			return null;
		}

		return tweets.get(user).values();
	}

	private void init() {
		tweets = new HashMap<String, Map<Long, Tweet>>();
		followeds = new HashMap<String, Map<String, TwitterUser>>();
	}
}
