package com.dudko.highwave.utils;

import java.io.IOException;
import java.net.*;
import java.util.*;

import javax.xml.parsers.*;

import org.joda.time.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class NationalBankOfUkraineServiceClient {
	public static Map<String, Double> getExchangeRatesOnDate(LocalDate date) {
		Map<String, Double> map = new HashMap<String, Double>();

		URL xmlUrl;
		try {
			String xmlUrlPattern = String.format("http://buhgalter911.com/Services/ExchangeRateNBU.asmx/GetRates?date=%s",
					date.toString("MM/dd/yyyy"));
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

		NodeList nodeList = document.getElementsByTagName("DocumentElement").item(0).getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			org.w3c.dom.Node node = nodeList.item(i);

			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				Element element = (Element) node;

				String currencyCharCode = element.getElementsByTagName("CodeLit").item(0).getChildNodes().item(0).getNodeValue();
				String currencyRate = element.getElementsByTagName("Exch").item(0).getChildNodes().item(0).getNodeValue();

				map.put(currencyCharCode, Double.parseDouble(currencyRate));
			}
		}

		return map;
	}
}
