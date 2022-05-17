package name.herve.jtlm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.herve.jtlm.model.Tweet;
import name.herve.jtlm.model.TwitterUser;
import name.herve.jtlm.persistence.SimpleStorage;

public class ComputeStats {
	public final static long MIN = 1394995000000l;
	public final static long MIN_DIST1 = 60 * 1000;
	public final static long MIN_DIST2 = 2 * 60 * 1000;
	public final static long MIN_DIST5 = 5 * 60 * 1000;
	public final static long MIN_DIST10 = 10 * 60 * 1000;
	public final static long MIN_DIST15 = 15 * 60 * 1000;
	public final static long MIN_DIST20 = 20 * 60 * 1000;
	public final static long MIN_DIST25 = 25 * 60 * 1000;
	public final static long MIN_DIST30 = 30 * 60 * 1000;

	public static String hms(long t) {
		long h = t / (60 * 60 * 1000);
		t = t - h * 60 * 60 * 1000;
		long m = t / (60 * 1000);
		t = t - m * 60 * 1000;
		long s = t / 1000;
		return String.format("%02d:%02d:%02d", h, m, s);
	}

	public static void main(String[] args) {
		try {
			SimpleStorage storage = GetUserTweets.getStorage();
			DateFormat df = new SimpleDateFormat();

			for (String u : storage.getStoredUsers()) {
				List<Date> dates = new ArrayList<Date>();
				for (Tweet t : storage.getTweets(u)) {
					dates.add(t.getDate());
				}
				System.out.println(u + " size: " + dates.size() + " min: " + df.format(Collections.min(dates)));
			}

			Map<String, List<Long>> data = new HashMap<String, List<Long>>();

			for (String u : storage.getStoredUsers()) {
				List<Long> dates = new ArrayList<Long>();
				for (Tweet t : storage.getTweets(u)) {
					long tm = t.getDate().getTime();
					if (tm > MIN) {
						dates.add(tm);
					}
				}

				data.put(u, dates);
			}

			List<Long> master = data.get(args[0]);
			Map<String, Map<String, TwitterUser>> ff = storage.getFolloweds();
			Map<String, TwitterUser> masterFF = ff.get(args[0]);

			System.out.println("Working on " + args[0] + " : " + master.size() + " tweets, following " + masterFF.size() + " accounts");

			for (String u : storage.getStoredUsers()) {
				if (u.equals(args[0])) {
					continue;
				}
				List<Long> ud = data.get(u);
				double avgDist = 0;
				int nbVeryClose1 = 0;
				int nbVeryClose2 = 0;
				int nbVeryClose5 = 0;
				int nbVeryClose10 = 0;
				int nbVeryClose15 = 0;
				int nbVeryClose20 = 0;
				int nbVeryClose25 = 0;
				int nbVeryClose30 = 0;
				for (long target : master) {
					long closestDist = Long.MAX_VALUE;

					for (long d : ud) {
						long dist = Math.abs(target - d);
						if (dist < closestDist) {
							closestDist = dist;
						}
					}

					avgDist += closestDist;
					if (closestDist < MIN_DIST30) {
						nbVeryClose30++;
						if (closestDist < MIN_DIST25) {
							nbVeryClose25++;
							if (closestDist < MIN_DIST20) {
								nbVeryClose20++;
								if (closestDist < MIN_DIST15) {
									nbVeryClose15++;
									if (closestDist < MIN_DIST10) {
										nbVeryClose10++;
										if (closestDist < MIN_DIST5) {
											nbVeryClose5++;
											if (closestDist < MIN_DIST2) {
												nbVeryClose2++;
												if (closestDist < MIN_DIST1) {
													nbVeryClose1++;
												}
											}
										}
									}
								}
							}
						}
					}
				}
				avgDist /= master.size();

				int alsoFollowing = 0;
				Map<String, TwitterUser> ffu = ff.get(u);
				for (String ffm : masterFF.keySet()) {
					if (ffu.containsKey(ffm)) {
						alsoFollowing++;
					}
				}

				double pct = alsoFollowing / (double) ffu.size();

				System.out.println(u + ", " + ud.size() + ", " + alsoFollowing + ", " + pct + ", " + hms((long) avgDist) + ", " + nbVeryClose1 + ", " + nbVeryClose2 + ", " + nbVeryClose5 + ", " + nbVeryClose10 + ", " + nbVeryClose15 + ", " + nbVeryClose20 + ", " + nbVeryClose25 + ", " + nbVeryClose30);
			}

		} catch (JTLMException e) {
			e.printStackTrace();
		}
	}

}
