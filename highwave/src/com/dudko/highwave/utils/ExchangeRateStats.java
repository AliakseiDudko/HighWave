package com.dudko.highwave.utils;

import java.util.Map;

import org.joda.time.DateTime;

import com.dudko.highwave.deposit.Currency;

public class ExchangeRateStats {
	public static float currentUsdExchangeRate;
	public static float dailyUsdExchangeRateDelta;

	static {
		resetExchangeRateStats();
	}

	public static void resetExchangeRateStats() {
		DateTime today = NationalBankServiceClient.getLastDailyExRatesDate();

		Map<String, Double> todayStats = NationalBankServiceClient.getExchangeRatesOnDate(today);
		Double todayUsdExchangeRate = todayStats.get(Currency.USD.toString());

		Map<String, Double> monthAgoStats = NationalBankServiceClient.getExchangeRatesOnDate(today.minusMonths(1));
		Double monthAgoUsdExchangeRate = monthAgoStats.get(Currency.USD.toString());

		currentUsdExchangeRate = (float) (todayUsdExchangeRate + 20.0f);
		dailyUsdExchangeRateDelta = (float) (todayUsdExchangeRate - monthAgoUsdExchangeRate) / 30.0f;
	}
}
