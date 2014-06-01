package name.herve.jtlm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import name.herve.jtlm.gui.StatusListener;
import name.herve.jtlm.model.TwitterFriends;
import name.herve.jtlm.model.TwitterList;
import twitter4j.User;

public class JTLMApplication {
	private TwitterWrapper tw;

	private Set<StatusListener> statusListeners;

	public JTLMApplication() {
		super();

		statusListeners = new HashSet<StatusListener>();
	}

	public boolean addStatusListener(StatusListener l) {
		return statusListeners.add(l);
	}

	public void fillListMembers(TwitterList list) throws JTLMException {
		tw.fillListMembers(list);
	}

	public TwitterFriends getFriends(String who) throws JTLMException {
		return tw.getFriends(who);
	}

	public List<TwitterList> getLists() throws JTLMException {
		return tw.getLists();
	}

	public void init() throws JTLMException {
		tw = new TwitterWrapper();
		tw.init();
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

	public User getAuthenticatedUser() {
		return tw.getAuthenticatedUser();
	}

}
