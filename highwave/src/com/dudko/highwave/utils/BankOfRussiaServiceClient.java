package com.dudko.highwave.utils;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.parsers.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.dudko.highwave.deposit.Currency;

public class BankOfRussiaServiceClient {
	public static List<Entry<DateTime, Double>> getExchangeRateHistory(Currency currency) {
		List<Entry<DateTime, Double>> list = new ArrayList<Entry<DateTime, Double>>();

		DateTime endDate = DateTime.now();
		DateTime startDate = endDate.minusYears(1);

		URL xmlUrl;
		try {
			String currencyUniqueCode = "";
			switch (currency) {
			case USD:
				currencyUniqueCode = "R01235";
				break;
			case EUR:
				currencyUniqueCode = "R01239";
				break;
			default:
				throw new IllegalArgumentException(String.format("Currency %s is not supported.", currency.toString()));
			}

			String xmlUrlPattern = String.format("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=%s&date_req2=%s&VAL_NM_RQ=%s",
					startDate.toString("dd/MM/yyyy"), endDate.toString("dd/MM/yyyy"), currencyUniqueCode);
			xmlUrl = new URL(xmlUrlPattern);
		} catch (MalformedURLException exception) {
			exception.printStackTrace();
			return list;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException exception) {
			exception.printStackTrace();
			return list;
		}

		Document document;
		try {
			document = builder.parse(xmlUrl.openStream());
		} catch (SAXException | IOException exception) {
			exception.printStackTrace();
			return list;
		}

		NodeList nodeList = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			org.w3c.dom.Node node = nodeList.item(i);

			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				Element element = (Element) node;

				String onDateString = element.getAttribute("Date");
				DateTime onDate = DateTime.parse(onDateString, DateTimeFormat.forPattern("dd.MM.yyyy"));

				String currencyRateString = element.getElementsByTagName("Value").item(0).getChildNodes().item(0).getNodeValue();
				currencyRateString = currencyRateString.replace(',', '.');
				Double currencyRate = Double.parseDouble(currencyRateString);

				list.add(new AbstractMap.SimpleEntry<DateTime, Double>(onDate, currencyRate));
			}
		}

		return list;
	}
}
