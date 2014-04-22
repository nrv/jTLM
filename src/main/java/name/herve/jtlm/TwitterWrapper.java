package name.herve.jtlm;

import java.util.ArrayList;
import java.util.List;

import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;

public class TwitterWrapper {
	private Twitter twitter;
	private User me;

	public void fillListMembers(TwitterList list) throws JTLMException {
		try {
			long cursor = -1;
			PagableResponseList<User> usres;
			list.clearMembers();
			do {
				usres = twitter.getUserListMembers(list.getId(), cursor);
				for (User user : usres) {
					list.addMember(pojo(user));
				}
			} while ((cursor = usres.getNextCursor()) != 0);
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public TwitterUserCollection getFriends() throws JTLMException {
		try {
			TwitterFriends friends = new TwitterFriends();
			friends.setSize(me.getFriendsCount());

			List<Long> allIds = new ArrayList<Long>();
			long cursor = -1;
			IDs ids;
			do {
				ids = twitter.getFriendsIDs(cursor);
				for (long id : ids.getIDs()) {
					allIds.add(id);
				}
			} while ((cursor = ids.getNextCursor()) != 0);

			int ps = 100;
			int np = (int) Math.ceil((double) allIds.size() / (double) ps);
			for (int p = 0; p < np; p++) {
				int offset = p * ps;
				int nb = Math.min(ps, allIds.size() - offset);
				long[] qids = new long[nb];
				for (int i = 0; i < nb; i++) {
					qids[i] = allIds.get(offset + i);
				}

				ResponseList<User> users = twitter.lookupUsers(qids);
				for (User user : users) {
					friends.addMember(pojo(user));
				}
			}

			return friends;
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public List<TwitterList> getLists() throws JTLMException {
		try {
			List<TwitterList> res = new ArrayList<TwitterList>();

			ResponseList<UserList> lists = twitter.getUserLists(me.getScreenName());
			for (UserList list : lists) {
				res.add(pojo(list));
			}

			return res;
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public void init() throws JTLMException {
		twitter = new TwitterFactory().getInstance();
	}

	private TwitterUser pojo(User user) {
		TwitterUser pojo = new TwitterUser();
		pojo.setId(user.getId());
		pojo.setName(user.getName());
		pojo.setScreenName(user.getScreenName());
		pojo.setDescription(user.getDescription());
		pojo.setImageUrl(user.getBiggerProfileImageURL());
		return pojo;
	}

	private TwitterList pojo(UserList list) {
		TwitterList pojo = new TwitterList();
		pojo.setId(list.getId());
		pojo.setName(list.getName());
		pojo.setDescription(list.getDescription());
		pojo.setSize(list.getMemberCount());
		return pojo;
	}

	public void start() throws JTLMException {
		try {
			me = twitter.verifyCredentials();
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public void stop() {
	}
}
