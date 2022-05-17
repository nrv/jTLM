package name.herve.jtlm.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import name.herve.jtlm.JTLMException;
import name.herve.jtlm.model.TwitterFriends;
import name.herve.jtlm.model.TwitterList;
import name.herve.jtlm.model.TwitterUser;

public class JTLMData implements Serializable {
	private static final long serialVersionUID = -2225732093791633494L;

	public static JTLMData load(File f) throws JTLMException {
		try {
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			JTLMData data = (JTLMData) ois.readObject();
			ois.close();
			return data;
		} catch (ClassNotFoundException e) {
			throw new JTLMException(e);
		} catch (IOException e) {
			throw new JTLMException(e);
		}
	}

	public static void save(JTLMData data, File f) throws JTLMException {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
		} catch (IOException e) {
			throw new JTLMException(e);
		}
	}

	private List<TwitterList> lists;

	private TwitterFriends friends;

	public JTLMData() {
		super();
		lists = new ArrayList<TwitterList>();
		friends = new TwitterFriends();
	}
	
	public void removeUser(TwitterUser u) {
		friends.removeMember(u.getScreenName());
	}

	public TwitterFriends getFriends() {
		return friends;
	}

	public List<TwitterList> getLists() {
		return lists;
	}

	public void setFriends(TwitterFriends friends) {
		this.friends = friends;
	}

	public void setLists(List<TwitterList> lists) {
		this.lists = lists;
	}

}
