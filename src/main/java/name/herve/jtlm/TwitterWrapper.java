package name.herve.jtlm;

import java.util.ArrayList;
import java.util.List;

import name.herve.jtlm.model.Tweet;
import name.herve.jtlm.model.TwitterFriends;
import name.herve.jtlm.model.TwitterList;
import name.herve.jtlm.model.TwitterUser;
import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;

public class TwitterWrapper {
	private Twitter twitter;
	private User authenticatedUser;

	public void addToList(TwitterList list, List<TwitterUser> users) throws JTLMException {
		try {
			long[] ids = new long[users.size()];
			for (int i = 0; i < users.size(); i++) {
				ids[i] = users.get(i).getId();
			}

			twitter.createUserListMembers(list.getId(), ids);

		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public void addToList(TwitterList list, TwitterUser user) throws JTLMException {
		try {
			twitter.createUserListMember(list.getId(), user.getId());
			list.addMember(user);
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

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

	public User getAuthenticatedUser() {
		return authenticatedUser;
	}

	public TwitterFriends getFriends(String who) throws JTLMException {
		try {
			TwitterFriends friends = new TwitterFriends();

			List<Long> allIds = new ArrayList<>();
			long cursor = -1;
			IDs ids;
			do {
				ids = twitter.getFriendsIDs(who, cursor);
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

			friends.setSize(friends.getMembersSize());

			return friends;
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public List<Tweet> getLastTweets(Long userId) throws JTLMException {
		try {
			List<Tweet> tweets = new ArrayList<>();
			List<Status> statuses = twitter.getUserTimeline(userId);
			for (Status s : statuses) {
				tweets.add(pojo(s));
			}
			System.out.println("getTweets(" + userId + ") : " + tweets.size());

			return tweets;
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public List<TwitterList> getLists() throws JTLMException {
		try {
			List<TwitterList> res = new ArrayList<>();

			ResponseList<UserList> lists = twitter.getUserLists(authenticatedUser.getScreenName());
			for (UserList list : lists) {
				res.add(pojo(list));
			}

			return res;
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public List<Tweet> getTweets(String user) throws JTLMException {
		try {
			List<Tweet> tweets = new ArrayList<>();
			final int nbPerPage = 200;
			int page = 1;
			int retrieved;
			do {
				Paging paging = new Paging(page, nbPerPage);
				List<Status> statuses = twitter.getUserTimeline(user, paging);
				if (statuses != null) {
					retrieved = statuses.size();
					for (Status s : statuses) {
						tweets.add(pojo(s));
					}
				} else {
					retrieved = 0;
				}
				System.out.println("getTweets(" + user + ") page " + page + " : " + retrieved);
				page++;
			} while (retrieved > 0);

			return tweets;
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public void init() throws JTLMException {
		twitter = new TwitterFactory().getInstance();
	}

	public boolean isConnected() {
		return authenticatedUser != null;
	}

	private Tweet pojo(Status status) {
		Tweet pojo = new Tweet();
		pojo.setId(status.getId());
		pojo.setDate(status.getCreatedAt());
		pojo.setRetweet(status.isRetweet());
		pojo.setText(status.getText());
		return pojo;
	}

	private TwitterUser pojo(User user) {
		if (user == null) {
			return null;
		}
		TwitterUser pojo = new TwitterUser();
		pojo.setId(user.getId());
		pojo.setName(user.getName());
		pojo.setScreenName(user.getScreenName());
		pojo.setDescription(user.getDescription());
		pojo.setImageUrl(user.getMiniProfileImageURL());
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

	public void removeFromList(TwitterList list, TwitterUser user) throws JTLMException {
		try {
			twitter.destroyUserListMember(list.getId(), user.getId());
			list.removeMember(user.getScreenName());
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public void start() throws JTLMException {
		try {
			authenticatedUser = twitter.verifyCredentials();
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}

	public void stop() {
	}

	public TwitterUser unfollow(Long userId) throws JTLMException {
		try {
			User user = twitter.destroyFriendship(userId);
			return pojo(user);
		} catch (TwitterException e) {
			throw new JTLMException(e);
		}
	}
}
