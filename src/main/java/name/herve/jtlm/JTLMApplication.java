package name.herve.jtlm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import name.herve.jtlm.gui.StatusListener;
import name.herve.jtlm.model.Tweet;
import name.herve.jtlm.model.TwitterFriends;
import name.herve.jtlm.model.TwitterList;
import name.herve.jtlm.model.TwitterUser;
import twitter4j.User;

public class JTLMApplication {
	private TwitterWrapper tw;

	private Set<StatusListener> statusListeners;

	private Map<TwitterList, List<TwitterUser>> toAddToLists;

	public JTLMApplication() {
		super();

		statusListeners = new HashSet<>();

		toAddToLists = new HashMap<>();
	}

	public boolean addStatusListener(StatusListener l) {
		return statusListeners.add(l);
	}

	public void addToList(TwitterList list, List<TwitterUser> users) throws JTLMException {
		tw.addToList(list, users);
		System.out.println("Added to list " + list.getName() + " : ");
		for (TwitterUser u : users) {
			System.out.println(" ~ " + u.getScreenName());
		}
	}

	public void addToList(TwitterList list, TwitterUser user) throws JTLMException {
		if (!isConnected()) {
			System.err.println("Not connected !");
			return;
		}
		tw.addToList(list, user);
		System.out.println(user.getScreenName() + " added to list " + list.getName());
	}

	public void addToListAsync(TwitterList list, TwitterUser user) throws JTLMException {
		if (!toAddToLists.containsKey(list)) {
			toAddToLists.put(list, new ArrayList<TwitterUser>());
		}

		toAddToLists.get(list).add(user);
		list.addMember(user);
		System.out.println("[ASYNC] " + user.getScreenName() + " added to list " + list.getName());
	}

	public void fillListMembers(TwitterList list) throws JTLMException {
		tw.fillListMembers(list);
	}

	public User getAuthenticatedUser() {
		return tw.getAuthenticatedUser();
	}

	public TwitterFriends getFriends(String who) throws JTLMException {
		return tw.getFriends(who);
	}

	public List<Tweet> getLastTweets(Long userId) throws JTLMException {
		return tw.getLastTweets(userId);
	}

	public List<TwitterList> getLists() throws JTLMException {
		return tw.getLists();
	}

	public void init() throws JTLMException {
		tw = new TwitterWrapper();
		tw.init();
	}

	public boolean isConnected() {
		return (tw != null) && tw.isConnected();
	}

	public void removeFromList(TwitterList list, TwitterUser user) throws JTLMException {
		if (!isConnected()) {
			System.err.println("Not connected !");
			return;
		}
		tw.removeFromList(list, user);
		System.out.println(user.getScreenName() + " removed from list " + list.getName());
	}

	public boolean removeStatusListener(StatusListener l) {
		return statusListeners.remove(l);
	}

	public void setStatusMessage(String statusMessage) {
		for (StatusListener l : statusListeners) {
			l.statusChanged(statusMessage);
		}
	}

	public void start() throws JTLMException {
		tw.start();
	}

	public void stop() {
		tw.stop();
	}

	public void syncLists() throws JTLMException {
		for (Entry<TwitterList, List<TwitterUser>> e : toAddToLists.entrySet()) {
			addToList(e.getKey(), e.getValue());
		}

		toAddToLists.clear();
	}

	public TwitterUser unfollow(Long userId) throws JTLMException {
		return tw.unfollow(userId);
	}

}
