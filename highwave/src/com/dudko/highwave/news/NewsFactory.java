package com.dudko.highwave.news;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class NewsFactory {
	private static final Twitter twitter;

	static {
		twitter = new TwitterFactory().getInstance();
	}

	public List<Status> getNewsFeed() {

		List<Status> statuses;
		try {
			statuses = twitter.getHomeTimeline();
		} catch (TwitterException e) {
			statuses = new ArrayList<Status>();
			e.printStackTrace();
		}

		return statuses;
	}
}