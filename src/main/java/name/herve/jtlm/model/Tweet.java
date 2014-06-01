package name.herve.jtlm.model;

import java.io.Serializable;
import java.util.Date;

public class Tweet implements Serializable {
	private static final long serialVersionUID = 1121058961102545710L;

	private Date date;
	private long id;
	private String text;
	private boolean retweet;

	public Date getDate() {
		return date;
	}

	public long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public boolean isRetweet() {
		return retweet;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setRetweet(boolean retweet) {
		this.retweet = retweet;
	}

	public void setText(String text) {
		this.text = text;
	}
}
