package name.herve.jtlm.model;

import java.io.Serializable;

public class TwitterUser implements Serializable {
	private static final long serialVersionUID = -5991040719486386849L;
	
	private long id;
	private String name;
	private String screenName;
	private String description;
	private String imageUrl;

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getName() {
		return name;
	}

	public String getScreenName() {
		return screenName;
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

	public TwitterUser setName(String name) {
		this.name = name;
		return this;
	}

	public TwitterUser setScreenName(String screenName) {
		this.screenName = screenName;
		return this;
	}
}
