package com.dudko.highwave.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dudko.highwave.deposit.Currency;

public class ExchangeRateStats {
	public static float currentUsdExchangeRate;
	public static float dailyUsdExchangeRateDelta;

	static {
		resetExchangeRateStats();
	}

	public static void resetExchangeRateStats() {
		DateTimeZone minskZone = DateTimeZone.forID("Europe/Minsk");
		DateTime today = DateTime.now(minskZone).plusDays(1);

		Map<String, Double> todayStats = getExchangeRatesOnDate(today);
		Double todayUsdExchangeRate = todayStats.get(Currency.USD.toString());

		Map<String, Double> monthAgoStats = getExchangeRatesOnDate(today.minusMonths(1));
		Double monthAgoUsdExchangeRate = monthAgoStats.get(Currency.USD.toString());

		currentUsdExchangeRate = (float) (todayUsdExchangeRate + 20.0f);
		dailyUsdExchangeRateDelta = (float) (todayUsdExchangeRate - monthAgoUsdExchangeRate) / 30.0f;
	}

	public static Map<String, Double> getExchangeRatesOnDate(DateTime date) {
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
