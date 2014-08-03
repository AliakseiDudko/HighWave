package com.dudko.highwave.cron;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dudko.highwave.utils.ExchangeRateStats;

@SuppressWarnings("serial")
public class ResetExchangeRateStatsServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		ExchangeRateStats.resetExchangeRateStats();
	}
}