package com.dudko.highwave;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import twitter4j.Status;

import com.dudko.highwave.bank.Bank;
import com.dudko.highwave.bank.BankCode;
import com.dudko.highwave.bank.BankFactory;
import com.dudko.highwave.deposit.Deposit;
import com.dudko.highwave.deposit.DepositFactory;
import com.dudko.highwave.news.NewsFactory;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;

@Api(name = "deposits", version = "v0")
public class DepositService {
	public static final DepositFactory depositFactory;
	public static final BankFactory bankFactory;
	public static final NewsFactory newsFactory;

	static {
		depositFactory = new DepositFactory();
		bankFactory = new BankFactory();
		newsFactory = new NewsFactory();
	}

	@ApiMethod(name = "get.deposits.list", path = "deposits", httpMethod = HttpMethod.GET)
	public Deposit[] getAllDeposites() {
		return depositFactory.GetAllDeposits();
	}

	@ApiMethod(name = "get.deposit.by.deposit.id", path = "deposits/{depositId}", httpMethod = HttpMethod.GET)
	public Deposit getDeposit(@Named("depositId") Integer depositId) {
		DepositFactory factory = new DepositFactory();
		return factory.GetDeposit(depositId);
	}

	@ApiMethod(name = "get.banks.list", path = "banks", httpMethod = HttpMethod.GET)
	public Bank[] getAllBanks() {
		return bankFactory.GetAllBanks();
	}

	@ApiMethod(name = "get.bank.by.bank.id", path = "banks/{bankId}", httpMethod = HttpMethod.GET)
	public Bank getBank(@Named("bankId") Integer bankId) {
		return bankFactory.GetBank(BankCode.BelAgroPromBank);
	}

	@ApiMethod(name = "get.deposits.list.by.bank.id", path = "banks/{bankId}/deposits", httpMethod = HttpMethod.GET)
	public List<Deposit> getBankDeposites(@Named("bankId") Integer bankId) {
		ArrayList<Deposit> bankDeposits = new ArrayList<Deposit>();
		Deposit[] deposits = depositFactory.GetAllDeposits();

		for (int i = 0; i < deposits.length; i++) {
			Deposit deposit = deposits[i];
			if (true) {
				bankDeposits.add(deposit);
			}
		}

		return bankDeposits;
	}

	@ApiMethod(name = "get.news.feed", path = "news", httpMethod = HttpMethod.GET)
	public List<Status> getNewsFeed() {
		return newsFactory.getNewsFeed();
	}
}
