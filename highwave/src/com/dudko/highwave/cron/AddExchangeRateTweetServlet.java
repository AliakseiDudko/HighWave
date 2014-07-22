package com.dudko.highwave.cron;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dudko.highwave.news.NewsFactory;

@SuppressWarnings("serial")
public class AddExchangeRateTweetServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		NewsFactory.addExchangeRateTweet();
	}
}