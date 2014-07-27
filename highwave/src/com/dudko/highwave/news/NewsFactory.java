package com.dudko.highwave.news;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Seconds;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dudko.highwave.deposit.Currency;

import twitter4j.GeoLocation;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class NewsFactory {
	private static final Twitter twitter;
	private static List<Status> newsFeed;

	private static DateTime lastTimeGeneration;

	private static DateTimeZone minskZone;

	static {
		twitter = new TwitterFactory().getInstance();
		newsFeed = new ArrayList<Status>();

		minskZone = DateTimeZone.forID("Europe/Minsk");
		lastTimeGeneration = DateTime.now(minskZone).minusHours(1);
	}

	public static Status[] getNewsFeed() {
		Seconds generationInterval = Seconds.seconds(60);

		Seconds interval = Seconds.secondsBetween(lastTimeGeneration, DateTime.now(minskZone));
		if (interval.isGreaterThan(generationInterval)) {
			try {
				Paging paging = new Paging(1, 5);
				newsFeed = twitter.getHomeTimeline(paging);
				lastTimeGeneration = DateTime.now(minskZone);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		return newsFeed.toArray(new Status[newsFeed.size()]);
	}

	public static void addExchangeRateTweet() {
		DateTime today = DateTime.now(minskZone);

		Map<String, Double> map = getExchangeRatesOnDate(today);

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
		DateTime today = DateTime.now(minskZone);

		Map<String, Double> todayStats = getExchangeRatesOnDate(today);
		Map<String, Double> yesterdayStats = getExchangeRatesOnDate(today.minusDays(1));
		Map<String, Double> monthAgoStats = getExchangeRatesOnDate(today.minusMonths(1));
		Map<String, Double> yearAgoStats = getExchangeRatesOnDate(today.minusYears(1));

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

	private static Map<String, Double> getExchangeRatesOnDate(DateTime date) {
		Map<String, Double> map = new HashMap<String, Double>();

		URL xmlUrl;
		try {
			String xmlUrlPattern = "http://www.nbrb.by/Services/XmlExRates.aspx?onDate=" + date.toString("MM/dd/yyyy");
			xmlUrl = new URL(xmlUrlPattern);
		} catch (MalformedURLException exception) {
			exception.printStackTrace();
			return map;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException exception) {
			exception.printStackTrace();
			return map;
		}

		Document document;
		try {
			document = builder.parse(xmlUrl.openStream());
		} catch (SAXException | IOException exception) {
			exception.printStackTrace();
			return map;
		}

		NodeList nodeList = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;

				String currencyCharCode = element.getElementsByTagName("CharCode").item(0).getChildNodes().item(0).getNodeValue();
				String currencyRate = element.getElementsByTagName("Rate").item(0).getChildNodes().item(0).getNodeValue();

				map.put(currencyCharCode, Double.parseDouble(currencyRate));
			}
		}

		return map;
	}
}