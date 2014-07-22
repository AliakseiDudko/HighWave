package com.dudko.highwave;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import twitter4j.Status;

import com.dudko.highwave.bank.BankFactory;
import com.dudko.highwave.deposit.DepositAccount;
import com.dudko.highwave.deposit.DepositFactory;
import com.dudko.highwave.deposit.deposits.Deposit;
import com.dudko.highwave.news.NewsFactory;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;

@Api(name = "deposits", version = "v0")
public class DepositService {
	public static final DepositFactory depositFactory;
	public static final BankFactory bankFactory;

	static {
		depositFactory = new DepositFactory();
		bankFactory = new BankFactory();
	}

	@ApiMethod(name = "get.deposits.list", path = "deposits", httpMethod = HttpMethod.GET)
	public DepositAccount[] getAllDeposites(@Named("amount") float amount, @Named("period") int period) {
		List<DepositAccount> list = new ArrayList<DepositAccount>();

		for (Deposit deposit : depositFactory.GetAllDeposits()) {
			DepositAccount depositAccount = deposit.calculateDeposit(amount, period);
			list.add(depositAccount);
		}

		return list.toArray(new DepositAccount[list.size()]);
	}

	@ApiMethod(name = "get.news.feed", path = "news", httpMethod = HttpMethod.GET)
	public Status[] getNewsFeed() {
		return NewsFactory.getNewsFeed();
	}
}
