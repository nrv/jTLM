package name.herve.jtlm.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class TwitterUserCollection implements Iterable<TwitterUser> {
	private int size;
	private Map<String, TwitterUser> members;

	public TwitterUserCollection() {
		super();
		members = new HashMap<String, TwitterUser>();
	}
	
	public abstract String getName();

	public void addMember(TwitterUser tu) {
		members.put(tu.getScreenName(), tu);
	}

	public void clearMembers() {
		members.clear();
	}

	public int getMembersSize() {
		return members.size();
	}

	public int getSize() {
		return size;
	}

	@Override
	public Iterator<TwitterUser> iterator() {
		return members.values().iterator();
	}

	public void setSize(int size) {
		this.size = size;
	}
}
