package com.dudko.highwave.news;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Seconds;

import com.dudko.highwave.deposit.Currency;
import com.dudko.highwave.utils.NationalBankServiceClient;

import twitter4j.GeoLocation;
import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class NewsFactory {
	private static final Twitter twitter;
	private static List<OEmbed> newsFeed;

	private static DateTime lastTimeGeneration;

	static {
		twitter = new TwitterFactory().getInstance();
		newsFeed = new ArrayList<OEmbed>();

		lastTimeGeneration = DateTime.now().minusHours(1);
	}

	public static OEmbed[] getNewsFeed() {
		Seconds generationInterval = Seconds.seconds(60);

		Seconds interval = Seconds.secondsBetween(lastTimeGeneration, DateTime.now());
		if (interval.isGreaterThan(generationInterval)) {
			try {
				newsFeed.clear();

				Paging paging = new Paging(1, 5);
				for (Status tweet : twitter.getHomeTimeline(paging)) {
					OEmbedRequest req = new OEmbedRequest(tweet.getId(), "").HideMedia(false).MaxWidth(550);
					OEmbed oEmbed = twitter.getOEmbed(req);
					newsFeed.add(oEmbed);
				}

				lastTimeGeneration = DateTime.now();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		return newsFeed.toArray(new OEmbed[newsFeed.size()]);
	}

	public static void addExchangeRateTweet() {
		DateTime today = NationalBankServiceClient.GetLastDailyExRatesDate();

		Map<String, Double> map = NationalBankServiceClient.getExchangeRatesOnDate(today);

		String message = "Официальный курс рубля:\r\n";
		message += String.format("%s: %,.0f\r\n", Currency.USD.toString(), map.get(Currency.USD.toString()));
		message += String.format("%s: %,.0f\r\n", Currency.EUR.toString(), map.get(Currency.EUR.toString()));
		message += String.format("%s: %,.0f\r\n", Currency.RUB.toString(), map.get(Currency.RUB.toString()));

		String filePath = String.format("./assets/tweet/media/girls/%d.jpg", today.getDayOfMonth());
		File image = new File(filePath);

		GeoLocation location = new GeoLocation(53.900066d, 27.558531d);

		StatusUpdate tweet = new StatusUpdate(message);
		tweet.setMedia(image);
		tweet.setLocation(location);

		try {
			twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public static void addExchangeRateStatsTweet() {
		DateTime today = NationalBankServiceClient.GetLastDailyExRatesDate();

		Map<String, Double> todayStats = NationalBankServiceClient.getExchangeRatesOnDate(today);
		Map<String, Double> yesterdayStats = NationalBankServiceClient.getExchangeRatesOnDate(today.minusDays(1));
		Map<String, Double> monthAgoStats = NationalBankServiceClient.getExchangeRatesOnDate(today.minusMonths(1));
		Map<String, Double> yearAgoStats = NationalBankServiceClient.getExchangeRatesOnDate(today.minusYears(1));

		String usd = Currency.USD.toString();
		double yesterdayUsd = todayStats.get(usd) - yesterdayStats.get(usd);
		double monthAgoUsd = todayStats.get(usd) - monthAgoStats.get(usd);
		double yearAgoUsd = todayStats.get(usd) - yearAgoStats.get(usd);

		String eur = Currency.EUR.toString();
		double yesterdayEur = todayStats.get(eur) - yesterdayStats.get(eur);
		double monthAgoEur = todayStats.get(eur) - monthAgoStats.get(eur);
		double yearAgoEur = todayStats.get(eur) - yearAgoStats.get(eur);

		String rub = Currency.RUB.toString();
		double yesterdayRub = todayStats.get(rub) - yesterdayStats.get(rub);
		double monthAgoRub = todayStats.get(rub) - monthAgoStats.get(rub);
		double yearAgoRub = todayStats.get(rub) - yearAgoStats.get(rub);

		String message = "Изменение официального курса рубля за день/месяц/год:\r\n";
		message += String.format("%s: %+,.0f/%+,.0f/%+,.0f\r\n", usd, yesterdayUsd, monthAgoUsd, yearAgoUsd);
		message += String.format("%s: %+,.0f/%+,.0f/%+,.0f\r\n", eur, yesterdayEur, monthAgoEur, yearAgoEur);
		message += String.format("%s: %+,.0f/%+,.0f/%+,.0f\r\n", rub, yesterdayRub, monthAgoRub, yearAgoRub);

		String filePath = String.format("./assets/tweet/media/cats/%d.jpg", today.getDayOfMonth());
		File image = new File(filePath);

		GeoLocation location = new GeoLocation(53.900066d, 27.558531d);

		StatusUpdate tweet = new StatusUpdate(message);
		tweet.setMedia(image);
		tweet.setLocation(location);

		try {
			twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
}