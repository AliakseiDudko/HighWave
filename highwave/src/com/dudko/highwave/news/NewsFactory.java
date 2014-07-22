package com.dudko.highwave.news;

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
import org.joda.time.Seconds;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dudko.highwave.deposit.Currency;

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
		lastTimeGeneration = DateTime.now().minusHours(1);
	}

	public static Status[] getNewsFeed() {
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

		return newsFeed.toArray(new Status[newsFeed.size()]);
	}

	public static void addExchangeRateTweet() {
		Map<String, String> map = parseDailyExchangeRate();

		String tweet = "Официальный курс белорусского рубля:\n";
		tweet += Currency.USD + ": " + map.get(Currency.USD.toString()) + "\n";
		tweet += Currency.EUR + ": " + map.get(Currency.EUR.toString()) + "\n";
		tweet += Currency.RUB + ": " + map.get(Currency.RUB.toString()) + "\n";
		System.out.println(tweet);
		System.out.println(DateTime.now());

		try {
			twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, String> parseDailyExchangeRate() {
		Map<String, String> map = new HashMap<String, String>();

		URL xmlUrl;
		try {
			xmlUrl = new URL("http://www.nbrb.by/Services/XmlExRates.aspx");
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

				map.put(currencyCharCode, currencyRate);
			}
		}

		return map;
	}
}