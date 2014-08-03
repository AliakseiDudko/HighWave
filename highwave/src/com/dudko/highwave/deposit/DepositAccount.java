package com.dudko.highwave.deposit;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import utils.ExchangeRateStats;

import com.dudko.highwave.deposit.deposits.Deposit;

public class DepositAccount {
	public Deposit deposit;
	public float startAmount;
	public float endAmount;
	public float profit;
	public float profitRate;

	public float startAmountUsd;
	public float endAmountUsd;
	public float profitUsd;
	public float profitRateUsd;
	public float endUsdExchangeRate;

	public int period;

	public AccountStatementRecord[] accountStatement;

	public DepositAccount(Deposit deposit, List<AccountStatementRecord> accountStatement) {
		this.deposit = deposit;

		if (!accountStatement.isEmpty()) {
			AccountStatementRecord firstRecord = accountStatement.get(0);
			AccountStatementRecord lastRecord = accountStatement.get(accountStatement.size() - 1);
			DateTime startDate = AccountStatementRecord.dateFormatter.parseDateTime(firstRecord.date);
			DateTime endDate = AccountStatementRecord.dateFormatter.parseDateTime(lastRecord.date);
			this.period = Days.daysBetween(startDate, endDate).getDays();

			this.startAmount = firstRecord.amount;
			this.endAmount = lastRecord.amount;
			this.profit = this.endAmount - this.startAmount;
			this.profitRate = 100.0f * this.profit / this.startAmount;

			this.endUsdExchangeRate = ExchangeRateStats.currentUsdExchangeRate + this.period * ExchangeRateStats.dailyUsdExchangeRateDelta;
			this.startAmountUsd = this.startAmount / ExchangeRateStats.currentUsdExchangeRate;
			this.endAmountUsd = this.endAmount / this.endUsdExchangeRate;
			this.profitUsd = this.endAmountUsd - this.startAmountUsd;
			this.profitRateUsd = 100.0f * this.profitUsd / this.startAmountUsd;
		}

		this.accountStatement = accountStatement.toArray(new AccountStatementRecord[accountStatement.size()]);
	}
}
