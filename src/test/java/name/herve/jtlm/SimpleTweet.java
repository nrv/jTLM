package name.herve.jtlm;

import java.io.Serializable;
import java.util.Date;

public class SimpleTweet implements Serializable {
	private static final long serialVersionUID = 6577276572680486788L;
	
	private Date date;
	private long id;
	private String text;
	private String user;
	private String screenName;
	private String pp;

	public SimpleTweet() {
		super();
	}

	public Date getDate() {
		return date;
	}

	public long getId() {
		return id;
	}

	public String getPp() {
		return pp;
	}

	public String getText() {
		return text;
	}

	public String getUser() {
		return user;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPp(String pp) {
		this.pp = pp;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "SimpleTweet [date=" + date + ", id=" + id + ", text=" + text + ", user=" + screenName + "/" + user + ", pp=" + pp + "]";
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

}
