package name.herve.jtlm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.ina.research.amalia.model.tweet.SimpleTweet;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TestStream {
	public final static File storage1 = new File("/home/nherve/Travail/Documents/InaLab/player/demo_tweets/some_tweets.bin");
	public final static File storage2 = new File("/home/nherve/Travail/Documents/InaLab/player/demo_tweets/some_tweets_2.bin");
	public final static File storage3 = new File("/home/nherve/Travail/Documents/InaLab/player/demo_tweets/some_tweets_3.bin");
	public final static File amalia = new File("/home/nherve/Travail/Documents/InaLab/player/demo_tweets/some_tweets.json");
	public static void main(String[] args) {
		
		
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

		final List<SimpleTweet> allTweets = new ArrayList<SimpleTweet>();

		StatusListener listener = new twitter4j.StatusListener() {

			@Override
			public void onException(Exception ex) {
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			@Override
			public void onStatus(Status status) {
				SimpleTweet st = new SimpleTweet();
				st.setDate(status.getCreatedAt());
				st.setId(status.getId());
				st.setUser(status.getUser().getName());
				st.setScreenName(status.getUser().getScreenName());
				st.setPp(status.getUser().getProfileImageURL());
				st.setText(status.getText());

				allTweets.add(st);

				System.out.println(allTweets.size() + " :: " + st);

				if (allTweets.size() % 25 == 0) {
					try {
						System.out.println(" ------ DUMPING ------");
						FileOutputStream fos = new FileOutputStream(storage2);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(allTweets);
						oos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onStallWarning(StallWarning warning) {
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			}
		};

		twitterStream.addListener(listener);
		twitterStream.filter("#SRFCTFC");
	}

}
