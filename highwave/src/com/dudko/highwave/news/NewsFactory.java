package com.dudko.highwave.news;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.parsers.*;

import org.joda.time.*;
import org.joda.time.format.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

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

		StatusUpdate tweet = createTweet(message, TweetType.ExchangeRate);
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

		StatusUpdate tweet = createTweet(message, TweetType.ExchangeRateStats);
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

		TweetType tweetType = TweetType.Undefined;
		String message = "";
		if (isUsdRecord && isEurRecord) {
			message = "Курс рос. рубля продолжает искать дно:\r\n";
			message += String.format("%,.4f %s/%s\r\n", maxUsdEntry.getValue(), Currency.RUB.toString(), Currency.USD.toString());
			message += String.format("%,.4f %s/%s\r\n", maxEurEntry.getValue(), Currency.RUB.toString(), Currency.EUR.toString());
			message += "Сегодня рос. рублю было очень больно.";

			tweetType = TweetType.RussianRubleStatsUsdEur;
		} else if (isUsdRecord) {
			message = "Курс доллара достиг исторического максимума:\r\n";
			message += String.format("%,.4f %s/%s\r\n", maxUsdEntry.getValue(), Currency.RUB.toString(), Currency.USD.toString());
			message += "Сегодня рос. рублю было больно.";

			tweetType = TweetType.RussianRubleStatsUsd;
		} else if (isEurRecord) {
			message = "Курс евро достиг исторического максимума:\r\n";
			message += String.format("%,.4f %s/%s\r\n", maxEurEntry.getValue(), Currency.RUB.toString(), Currency.EUR.toString());
			message += "Сегодня рос. рублю было больно.";

			tweetType = TweetType.RussianRubleStatsEur;
		}

		if (isUsdRecord || isEurRecord) {
			try {
				StatusUpdate tweet = createTweet(message, tweetType);
				twitter.updateStatus(tweet);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	public static void addDevaluationStatsTweet() {
		DateTime today = DateTime.now();
		DateTime yearAgo = today.minusYears(1);

		String usd = Currency.USD.toString();

		Map<String, Double> byrTodayExRates = NationalBankServiceClient.getExchangeRatesOnDate(today);
		Map<String, Double> byrYearAgoExRates = NationalBankServiceClient.getExchangeRatesOnDate(yearAgo);
		double byrToday = byrTodayExRates.get(usd);
		double byrYearAgo = byrYearAgoExRates.get(usd);
		double byrDevaluation = 100.0f * (byrToday - byrYearAgo) / byrYearAgo;

		List<Entry<DateTime, Double>> rurHistory = BankOfRussiaServiceClient.getExchangeRateHistory(Currency.USD);
		double rurToday = rurHistory.get(rurHistory.size() - 1).getValue();
		double rurYearAgo = rurHistory.get(0).getValue();
		double rurDevaluation = 100.0f * (rurToday - rurYearAgo) / rurYearAgo;

		Map<String, Double> uahTodayExRates = NationalBankOfUkraineServiceClient.getExchangeRatesOnDate(today);
		Map<String, Double> uahYearAgoExRates = NationalBankOfUkraineServiceClient.getExchangeRatesOnDate(yearAgo);
		double uahToday = uahTodayExRates.get(usd);
		double uahYearAgo = uahYearAgoExRates.get(usd);
		double uahDevaluation = 100.0f * (uahToday - uahYearAgo) / uahYearAgo;

		String message = "Рост курса доллара за год:\r\n";
		message += String.format("Стабильная Беларусь: %+,.1f%%\r\n", byrDevaluation);
		message += String.format("Встающая с колен РФ: %+,.1f%%\r\n", rurDevaluation);
		message += String.format("Революционная Украина: %+,.1f%%\r\n", uahDevaluation);

		try {
			StatusUpdate tweet = createTweet(message, TweetType.DevaluationStats);
			twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	private static StatusUpdate createTweet(String message, TweetType tweetType) {
		StatusUpdate tweet = new StatusUpdate(message);

		Element mediaElement = getMediaElement();
		if (mediaElement != null) {
			org.w3c.dom.Node mediaNode = mediaElement.getElementsByTagName(tweetType.toString()).item(0);
			Element element = (Element) mediaNode;

			String value = element.getChildNodes().item(0).getNodeValue();
			String mediaType = element.getAttribute("Type");
			if (MediaType.Image.equalsIgnoreCase(mediaType)) {
				try {
					InputStream input = new URL(value).openStream();
					tweet.setMedia("BBX.jpg", input);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (MediaType.Link.equalsIgnoreCase(mediaType)) {
				String status = String.format("%s\r\n%s", message, value);
				tweet = new StatusUpdate(status);
			}
		}

		GeoLocation location = new GeoLocation(53.900066d, 27.558531d);
		tweet.setLocation(location);

		return tweet;
	}

	private static Element getMediaElement() {
		URL xmlUrl;
		try {
			xmlUrl = new URL("http://high-wave-595.appspot.com/assets/tweet/tweetMedia.xml");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException exception) {
			exception.printStackTrace();
			return null;
		}

		Document document;
		try {
			document = builder.parse(xmlUrl.openStream());
		} catch (SAXException | IOException exception) {
			exception.printStackTrace();
			return null;
		}

		NodeList nodeList = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			org.w3c.dom.Node node = nodeList.item(i);

			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				Element element = (Element) node;

				String dayString = element.getAttribute("Day");
				int day = Integer.parseInt(dayString);
				if (day == DateTime.now().getDayOfMonth()) {
					return element;
				}
			}
		}

		return null;
	}
}