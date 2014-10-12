package com.dudko.highwave;

import java.util.*;

import javax.inject.Named;

import twitter4j.OEmbed;

import com.dudko.highwave.deposit.*;
import com.dudko.highwave.deposit.deposits.Deposit;
import com.dudko.highwave.news.NewsFactory;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;

@Api(name = "deposits", version = "v0")
public class DepositService {
	public static final DepositFactory depositFactory;

	static {
		depositFactory = new DepositFactory();
	}

	@ApiMethod(name = "get.deposits.list", path = "deposits", httpMethod = HttpMethod.GET)
	public DepositAccount[] getAllDeposites(@Named("amount") float amount, @Named("period") int period) {
		List<DepositAccount> list = new ArrayList<DepositAccount>();

		for (Deposit deposit : depositFactory.GetAllDeposits()) {
			DepositAccount depositAccount = deposit.calculateDeposit(amount, period);
			if (depositAccount != null) {
				list.add(depositAccount);
			}
		}

		Comparator<DepositAccount> comparator = new Comparator<DepositAccount>() {
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
		Collections.sort(list, Collections.reverseOrder(comparator));

		return list.toArray(new DepositAccount[list.size()]);
	}

	@ApiMethod(name = "get.news.feed", path = "news", httpMethod = HttpMethod.GET)
	public OEmbed[] getNewsFeed() {
		return NewsFactory.getNewsFeed();
	}
}
