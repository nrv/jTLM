package name.herve.jtlm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.MetadataBlock;
import fr.ina.research.amalia.model.MetadataFactory;
import fr.ina.research.amalia.model.tweet.SimpleTweet;
import fr.ina.research.amalia.model.tweet.SimpleTweetFormater;

public class TweetToAmalia {

	public TweetToAmalia() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, AmaliaException {
		FileInputStream fis = new FileInputStream(TestStream.storage3);
		ObjectInputStream ois = new ObjectInputStream(fis);
		@SuppressWarnings("unchecked")
		List<SimpleTweet> allTweets = (List<SimpleTweet>) ois.readObject();
		ois.close();

//		fis = new FileInputStream(TestStream.storage2);
//		ois = new ObjectInputStream(fis);
//		@SuppressWarnings("unchecked")
//		List<SimpleTweet> allTweets2 = (List<SimpleTweet>) ois.readObject();
//		ois.close();
//
//		allTweets.addAll(allTweets2);
//
//		Collections.sort(allTweets, new Comparator<SimpleTweet>() {
//
//			@Override
//			public int compare(SimpleTweet o1, SimpleTweet o2) {
//				return (int) (o1.getDate().getTime() - o2.getDate().getTime());
//			}
//		});
//		
//		List<fr.ina.research.amalia.model.tweet.SimpleTweet> allTweets3 = new ArrayList<fr.ina.research.amalia.model.tweet.SimpleTweet>();
//		for (SimpleTweet st : allTweets) {
//			fr.ina.research.amalia.model.tweet.SimpleTweet nt = new fr.ina.research.amalia.model.tweet.SimpleTweet();
//			nt.setDate(st.getDate());
//			nt.setId(st.getId());
//			nt.setPp(st.getPp().replace("_mini", "_normal"));
//			nt.setScreenName(st.getScreenName());
//			nt.setText(st.getText());
//			nt.setUser(st.getUser());
//			allTweets3.add(nt);
//		}
		
//		FileOutputStream fos = new FileOutputStream(TestStream.storage3);
//		ObjectOutputStream oos = new ObjectOutputStream(fos);
//		oos.writeObject(allTweets3);
//		oos.close();

		GregorianCalendar sgc = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.FRANCE);
		sgc.set(2015, 11, 15, 21, 0, 0);
		sgc.set(Calendar.MILLISECOND, 0);

		GregorianCalendar egc = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.FRANCE);
		egc.set(2015, 11, 15, 22, 0, 0);
		egc.set(Calendar.MILLISECOND, 0);
//		
//		GregorianCalendar cgc = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.FRANCE);
//
//		List<SimpleTweet> filteredTweets = new ArrayList<SimpleTweet>();
//		for (SimpleTweet st : allTweets) {
//			cgc.setTime(st.getDate());
//			if (sgc.before(cgc) && cgc.before(egc)) {
//				filteredTweets.add(st);
//			}
//		}
//
//		long offset = sgc.getTimeInMillis(); //filteredTweets.get(0).getDate().getTime();
//		double duration = (egc.getTimeInMillis() - offset) / 1000; //(filteredTweets.get(filteredTweets.size() - 1).getDate().getTime() - offset) / 1000;
//		System.out.println("duration : " + duration);
//		int nbbin = 360;
//		double binsize = duration / nbbin;
//		int[] bin = new int[nbbin];
//		Arrays.fill(bin, 0);
//
//		MetadataBlock textMetadata = MetadataFactory.createMetadataBlock("tweets", MetadataType.SYNCHRONIZED_TEXT);
//		textMetadata.setVersion(1);
//		textMetadata.setAlgorithm("TweetToAmalia");
//		textMetadata.setProcessedNow();
//		textMetadata.setProcessor("Ina Research - Nicolas HERVE");
//		textMetadata.setRootLocalisationBlock(new RexTimeCode(0d), new RexTimeCode(duration));
//
//		for (SimpleTweet st : filteredTweets) {
//			RexTimeCode tcin = new RexTimeCode((double) (st.getDate().getTime() - offset) / 1000);
//			RexTimeCode tcout = new RexTimeCode(tcin.getSecond() + 1);
//			LocalisationBlock tb = MetadataFactory.createSynchronizedTextLocalisationBlock(tcin, tcout, st.getText());
//			tb.setThumb(st.getPp().replace("_mini", "_normal"));
//
//			textMetadata.addToRootLocalisationBlock(tb);
//
//			int b = (int) (tcin.getSecond() / binsize);
//			if (b == nbbin) {
//				b--;
//			}
//			bin[b]++;
//
//			System.out.println(st.getDate());
//		}
//
//		for (int b = 0; b < bin.length; b++) {
//			System.out.println(b + " : " + bin[b]);
//		}
//
//		MetadataBlock histo = MetadataFactory.createMetadataBlock("histo", MetadataBlock.MetadataType.HISTOGRAM, new RexTimeCode(duration));
//		histo.setVersion(1);
//		histo.setAlgorithm("TweetToAmalia");
//		histo.setProcessedNow();
//		histo.setProcessor("Ina Research - Nicolas HERVE");
//
//		histo.getRootLocalisationBlock().getDataBlock().addHistogram(bin, null);
//
//		List<MetadataBlock> blocks = new ArrayList<MetadataBlock>();
//		blocks.add(textMetadata);
//		blocks.add(histo);
		
		List<MetadataBlock> blocks = new SimpleTweetFormater().createTweetMetadataBlocks("tweets", "histo", true, sgc.getTime(), egc.getTime(), 360, allTweets);
		MetadataFactory.serializeToJsonFile(blocks, TestStream.amalia);
	}

}
