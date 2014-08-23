package com.dudko.highwave.utils;

import java.io.IOException;
import java.net.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.soap.*;

import org.joda.time.DateTime;
import org.w3c.dom.*;

public class NationalBankServiceClient {
	public static DateTime GetLastDailyExRatesDate() {
		DateTime today = DateTime.now();
		DateTime lastDailyExRatesDate = today.minusDays(1);

		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();

			MimeHeaders headers = soapMessage.getMimeHeaders();
			headers.addHeader("SOAPAction", "http://www.nbrb.by/LastDailyExRatesDate");

			soapMessage.saveChanges();

			SOAPMessage soapResponse = soapConnection.call(soapMessage, "http://www.nbrb.by/Services/ExRates.asmx");

			Element response = (Element) soapResponse.getSOAPBody().getElementsByTagName("LastDailyExRatesDateResponse").item(0);
			Element result = (Element) response.getElementsByTagName("LastDailyExRatesDateResult").item(0);
			String lastDailyExRatesDateString = result.getChildNodes().item(0).getNodeValue();

			lastDailyExRatesDate = DateTime.parse(lastDailyExRatesDateString);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return today.isBefore(lastDailyExRatesDate) ? today : lastDailyExRatesDate;
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
