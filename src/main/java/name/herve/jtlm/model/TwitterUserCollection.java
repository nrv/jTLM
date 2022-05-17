package name.herve.jtlm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class TwitterUserCollection implements Iterable<TwitterUser>, Serializable {
	private static final long serialVersionUID = 7529066428635791615L;
	
	private int size;
	private Map<String, TwitterUser> members;
	private List<TwitterUser> sorted;

	public TwitterUserCollection() {
		super();
		members = new HashMap<String, TwitterUser>();
		sorted = new ArrayList<TwitterUser>();
	}

	public void addMember(TwitterUser tu) {
		members.put(tu.getScreenName(), tu);
		sorted.add(tu);
	}

	public void clearMembers() {
		members.clear();
		sorted.clear();
	}

	public boolean contains(String key) {
		return members.containsKey(key);
	}

	public TwitterUser get(int index) {
		return sorted.get(index);
	}

	public int getMembersSize() {
		return members.size();
	}

	public abstract String getName();

	public int getSize() {
		return size;
	}

	@Override
	public Iterator<TwitterUser> iterator() {
		return sorted.iterator();
	}

	public void removeMember(String screenName) {
		TwitterUser removed = members.remove(screenName);
		if (removed != null) {
			sorted.remove(removed);
		}
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void sort() {
		Collections.sort(sorted, new Comparator<TwitterUser>() {

			@Override
			public int compare(TwitterUser o1, TwitterUser o2) {
				return o1.getScreenName().compareTo(o2.getScreenName());
			}
		});
	}
}
