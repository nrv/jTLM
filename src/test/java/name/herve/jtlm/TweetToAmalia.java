package name.herve.jtlm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.LocalisationBlock;
import fr.ina.research.amalia.model.MetadataBlock;
import fr.ina.research.amalia.model.MetadataFactory;
import fr.ina.research.amalia.model.MetadataBlock.MetadataType;
import fr.ina.research.rex.commons.tc.RexTimeCode;

public class TweetToAmalia {

	public TweetToAmalia() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, AmaliaException {
		FileInputStream fis = new FileInputStream(TestStream.storage1);
		ObjectInputStream ois = new ObjectInputStream(fis);
		@SuppressWarnings("unchecked")
		List<SimpleTweet> allTweets = (List<SimpleTweet>) ois.readObject();
		ois.close();
		
		fis = new FileInputStream(TestStream.storage2);
		ois = new ObjectInputStream(fis);
		@SuppressWarnings("unchecked")
		List<SimpleTweet> allTweets2 = (List<SimpleTweet>) ois.readObject();
		ois.close();
		
		allTweets.addAll(allTweets2);

		Collections.sort(allTweets, new Comparator<SimpleTweet>() {

			@Override
			public int compare(SimpleTweet o1, SimpleTweet o2) {
				return (int) (o1.getDate().getTime() - o2.getDate().getTime());
			}
		});

		long offset = allTweets.get(0).getDate().getTime();
		double duration = (allTweets.get(allTweets.size() - 1).getDate().getTime() - offset) / 1000;
		int nbbin = 200;
		double binsize = duration / nbbin;
		int[] bin = new int[nbbin];
		Arrays.fill(bin, 0);

		MetadataBlock textMetadata = MetadataFactory.createMetadataBlock("tweets", MetadataType.SYNCHRONIZED_TEXT);
		textMetadata.setVersion(1);
		textMetadata.setAlgorithm("TweetToAmalia");
		textMetadata.setProcessedNow();
		textMetadata.setProcessor("Ina Research - Nicolas HERVE");
		textMetadata.setRootLocalisationBlock(new RexTimeCode(0d), new RexTimeCode(duration));

		for (SimpleTweet st : allTweets) {
			RexTimeCode tcin = new RexTimeCode((double) (st.getDate().getTime() - offset) / 1000);
			RexTimeCode tcout = new RexTimeCode(tcin.getSecond() + 1);
			LocalisationBlock tb = MetadataFactory.createSynchronizedTextLocalisationBlock(tcin, tcout, st.getText());
			tb.setThumb(st.getPp().replace("_mini", "_normal"));

			textMetadata.addToRootLocalisationBlock(tb);

			int b = (int) (tcin.getSecond() / binsize);
			if (b == nbbin) {
				b--;
			}
			bin[b]++;
		}
		
		for (int b = 0; b < bin.length; b++) {
			System.out.println(b + " : " + bin[b]);
		}
		
		MetadataBlock histo = MetadataFactory.createMetadataBlock("histo", MetadataBlock.MetadataType.HISTOGRAM, new RexTimeCode(duration));
		histo.setVersion(1);
		histo.setAlgorithm("TweetToAmalia");
		histo.setProcessedNow();
		histo.setProcessor("Ina Research - Nicolas HERVE");
		
		histo.getRootLocalisationBlock().getDataBlock().addHistogram(bin, null);

		List<MetadataBlock> blocks = new ArrayList<MetadataBlock>();
		blocks.add(textMetadata);
		blocks.add(histo);
		
		MetadataFactory.serializeToJsonFile(blocks, TestStream.amalia);
	}

}
