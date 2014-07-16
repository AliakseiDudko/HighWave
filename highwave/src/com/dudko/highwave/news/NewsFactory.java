package com.dudko.highwave.news;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class NewsFactory {
	private static final Twitter twitter;
	private static List<Status> newsFeed;

	private static final Seconds generationInterval;
	private static DateTime lastTimeGeneration;

	static {
		twitter = new TwitterFactory().getInstance();
		newsFeed = new ArrayList<Status>();

		int seconds = 60;
		generationInterval = Seconds.seconds(seconds);
		lastTimeGeneration = DateTime.now().minusSeconds(seconds);
	}

	public List<Status> getNewsFeed() {
		Seconds interval = Seconds.secondsBetween(lastTimeGeneration, DateTime.now());
		if (interval.isGreaterThan(generationInterval)) {
			try {
				Paging paging = new Paging(1, 5);
				newsFeed = twitter.getHomeTimeline(paging);
				lastTimeGeneration = DateTime.now();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		return newsFeed;
	}
}