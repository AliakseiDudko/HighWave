package com.dudko.highwave.news;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import org.joda.time.*;
import org.joda.time.format.*;

import com.dudko.highwave.deposit.Currency;
import com.dudko.highwave.utils.*;

import twitter4j.*;

public class NewsFactory {
	private static final Twitter twitter;
	private static List<OEmbed> newsFeed;

	static {
		twitter = new TwitterFactory().getInstance();
		newsFeed = new ArrayList<OEmbed>();
		readNewsFeed();
	}

	public static OEmbed[] getNewsFeed() {
		return newsFeed.toArray(new OEmbed[newsFeed.size()]);
	}

	public static void readNewsFeed() {
		try {
			newsFeed.clear();

			Paging paging = new Paging(1, 3);
			for (Status tweet : twitter.getUserTimeline(paging)) {
				OEmbedRequest req = new OEmbedRequest(tweet.getId(), "").HideMedia(false).MaxWidth(550);
				OEmbed oEmbed = twitter.getOEmbed(req);
				newsFeed.add(oEmbed);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public static void addExchangeRateTweet() {
		DateTime lastDate = NationalBankServiceClient.getLastDailyExRatesDate();
		if (lastDate.isAfterNow()) {
			lastDate = DateTime.now();
		}

		Map<String, Double> map = NationalBankServiceClient.getExchangeRatesOnDate(lastDate);

		String message = String.format("Официальный курс рубля на %s:\r\n", DateTimeFormat.forPattern("dd/MM/yy").print(lastDate));
		message += String.format("%s: %,.0f\r\n", Currency.USD.toString(), map.get(Currency.USD.toString()));
		message += String.format("%s: %,.0f\r\n", Currency.EUR.toString(), map.get(Currency.EUR.toString()));
		message += String.format("%s: %,.1f\r\n", Currency.RUB.toString(), map.get(Currency.RUB.toString()));

		String filePath = String.format("./assets/tweet/media/girls/%d.jpg", lastDate.getDayOfMonth());
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
		DateTime lastDate = NationalBankServiceClient.getLastDailyExRatesDate();
		if (lastDate.isAfterNow()) {
			lastDate = DateTime.now();
		}

		Map<String, Double> todayStats = NationalBankServiceClient.getExchangeRatesOnDate(lastDate);
		Map<String, Double> yesterdayStats = NationalBankServiceClient.getExchangeRatesOnDate(lastDate.minusDays(1));
		Map<String, Double> monthAgoStats = NationalBankServiceClient.getExchangeRatesOnDate(lastDate.minusMonths(1));
		Map<String, Double> yearAgoStats = NationalBankServiceClient.getExchangeRatesOnDate(lastDate.minusYears(1));

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
		message += String.format("%s: %+,.1f/%+,.1f/%+,.1f\r\n", rub, yesterdayRub, monthAgoRub, yearAgoRub);

		String filePath = String.format("./assets/tweet/media/cats/%d.jpg", lastDate.getDayOfMonth());
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

	public static void addRussianRubleStatsTweet() {
		List<Entry<DateTime, Double>> usdHistory = BankOfRussiaServiceClient.getExchangeRateHistory(Currency.USD);
		Entry<DateTime, Double> firstUsdEntry = usdHistory.get(usdHistory.size() - 1);
		Entry<DateTime, Double> maxUsdEntry = firstUsdEntry;
		for (Entry<DateTime, Double> entry : usdHistory) {
			if (maxUsdEntry.getValue() < entry.getValue()) {
				maxUsdEntry = entry;
			}
		}
		boolean isUsdRecord = maxUsdEntry.getKey() == firstUsdEntry.getKey();

		List<Entry<DateTime, Double>> eurHistory = BankOfRussiaServiceClient.getExchangeRateHistory(Currency.EUR);
		Entry<DateTime, Double> firstEurEntry = eurHistory.get(eurHistory.size() - 1);
		Entry<DateTime, Double> maxEurEntry = firstEurEntry;
		for (Entry<DateTime, Double> entry : eurHistory) {
			if (maxEurEntry.getValue() < entry.getValue()) {
				maxEurEntry = entry;
			}
		}
		boolean isEurRecord = maxEurEntry.getKey() == firstEurEntry.getKey();

		String message = "";
		String filePath = "";
		GeoLocation location = new GeoLocation(53.900066d, 27.558531d);

		if (isUsdRecord && isEurRecord) {
			message = "Курс рос. рубля достиг исторического максимума\r\n";
			message += String.format("%,.4f %s/%s\r\n", maxUsdEntry.getValue(), Currency.RUB.toString(), Currency.USD.toString());
			message += String.format("%,.4f %s/%s\r\n", maxEurEntry.getValue(), Currency.RUB.toString(), Currency.EUR.toString());
			message += "Сегодня рос. рублю было очень больно.\r\n";
			message += "https://www.youtube.com/watch?v=ZfP9_ZAofrY";
		} else if (isUsdRecord) {
			message = "Курс рос. рубля достиг исторического максимума\r\n";
			message += String.format("%,.4f %s/%s\r\n", maxUsdEntry.getValue(), Currency.RUB.toString(), Currency.USD.toString());
			message += "Сегодня рос. рублю было больно.";
			filePath = "./assets/tweet/media/rur/usd.jpg";
		} else if (isEurRecord) {
			message = "Курс рос. рубля достиг исторического максимума\r\n";
			message += String.format("%,.4f %s/%s\r\n", maxEurEntry.getValue(), Currency.RUB.toString(), Currency.EUR.toString());
			message += "Сегодня рос. рублю было больно.";
			filePath = "./assets/tweet/media/rur/eur.jpg";
		}

		if (isUsdRecord || isEurRecord) {
			StatusUpdate tweet = new StatusUpdate(message);
			tweet.setLocation(location);

			if (filePath != null && !filePath.isEmpty()) {
				File image = new File(filePath);
				tweet.setMedia(image);
			}

			try {
				twitter.updateStatus(tweet);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}
}