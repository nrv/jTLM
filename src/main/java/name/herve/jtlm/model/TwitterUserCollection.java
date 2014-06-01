package name.herve.jtlm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class TwitterUserCollection implements Iterable<TwitterUser> {
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
