package name.herve.jtlm.model;

import java.io.Serializable;

public class TwitterUser implements Serializable {
	private static final long serialVersionUID = -5991040719486386849L;

	private long id;

	private String name;
	private String screenName;
	private String description;
	private String imageUrl;
	private String lastTweet1;
	private String lastTweet20;
	private boolean lastTweetDone;

	public TwitterUser() {
		super();
		setLastTweetDone(false);
	}

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getLastTweet1() {
		return lastTweet1;
	}

	public String getLastTweet20() {
		return lastTweet20;
	}

	public String getName() {
		return name;
	}

	public String getScreenName() {
		return screenName;
	}

	public boolean isLastTweetDone() {
		return lastTweetDone;
	}

	public TwitterUser setDescription(String description) {
		this.description = description;
		return this;
	}

	public TwitterUser setId(long id) {
		this.id = id;
		return this;
	}

	public TwitterUser setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
		return this;
	}

	public void setLastTweet1(String lastTweet1) {
		this.lastTweet1 = lastTweet1;
	}

	public void setLastTweet20(String lastTweet20) {
		this.lastTweet20 = lastTweet20;
	}

	public void setLastTweetDone(boolean lastTweetDone) {
		this.lastTweetDone = lastTweetDone;
	}

	public TwitterUser setName(String name) {
		this.name = name;
		return this;
	}

	public TwitterUser setScreenName(String screenName) {
		this.screenName = screenName;
		return this;
	}
}
