package com.dudko.highwave;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Named;

import twitter4j.OEmbed;

import com.dudko.highwave.deposit.*;
import com.dudko.highwave.deposit.deposits.Deposit;
import com.dudko.highwave.news.NewsFactory;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;

@Api(name = "deposits", version = "v0")
public class DepositService {
	private static final DepositFactory depositFactory;
	private static final Comparator<DepositAccount> depositComparator;

	static {
		depositFactory = new DepositFactory();
		depositComparator = new Comparator<DepositAccount>() {
			public int compare(DepositAccount o1, DepositAccount o2) {
				if (o1.profitPerDay < o2.profitPerDay) {
					return -1;
				} else if (o1.profitPerDay > o2.profitPerDay) {
					return 1;
				} else {
					return 0;
				}
			}
		};
	}

	@ApiMethod(name = "get.deposits.list", path = "deposits", httpMethod = HttpMethod.GET)
	public DepositAccount[] getAllDeposites(@Named("amount") long amount, @Named("period") int period, @Named("currency") Currency currency) {
		List<DepositAccount> list = new ArrayList<DepositAccount>();

		for (Deposit deposit : depositFactory.GetAllDeposits(currency)) {
			DepositAccount depositAccount = deposit.calculateDeposit(amount, period);
			if (depositAccount != null) {
				list.add(depositAccount);
			}
		}

		Collections.sort(list, Collections.reverseOrder(depositComparator));

		return list.toArray(new DepositAccount[list.size()]);
	}

	@ApiMethod(name = "get.news.feed", path = "news", httpMethod = HttpMethod.GET)
	public OEmbed[] getNewsFeed() {
		return NewsFactory.getNewsFeed();
	}
}
