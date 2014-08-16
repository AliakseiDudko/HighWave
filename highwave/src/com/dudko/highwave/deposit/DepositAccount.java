package com.dudko.highwave.deposit;

import java.util.List;

import org.joda.time.Days;

import com.dudko.highwave.deposit.deposits.Deposit;
import com.dudko.highwave.utils.ExchangeRateStats;

public class DepositAccount {
	public Deposit deposit;
	public float startAmount;
	public float endAmount;
	public float profit;
	public float profitRate;
	public float profitPerDay;

	public float startAmountUsd;
	public float endAmountUsd;
	public float profitUsd;
	public float profitRateUsd;
	public float profitPerDayUsd;
	public float endUsdExchangeRate;

	public String startDate;
	public String endDate;
	public int period;

	public AccountStatementRecord[] accountStatement;

	public DepositAccount(Deposit deposit, List<AccountStatementRecord> accountStatement) {
		this.deposit = deposit;

		if (!accountStatement.isEmpty()) {
			AccountStatementRecord firstRecord = accountStatement.get(0);
			AccountStatementRecord lastRecord = accountStatement.get(accountStatement.size() - 1);
			this.startDate = firstRecord.date;
			this.endDate = lastRecord.date;
			this.period = Days.daysBetween(firstRecord.GetOriginalDate(), lastRecord.GetOriginalDate()).getDays();

			this.startAmount = roundUpTo(firstRecord.amount, 50);
			this.endAmount = roundUpTo(lastRecord.amount, 50);
			this.profit = this.endAmount - this.startAmount;
			this.profitRate = 100.0f * this.profit / this.startAmount;
			this.profitPerDay = this.profit / this.period;

			float endUsdDelta = this.period * ExchangeRateStats.dailyUsdExchangeRateDelta;
			this.endUsdExchangeRate = roundUpTo(ExchangeRateStats.currentUsdExchangeRate + endUsdDelta, 10);
			this.endAmountUsd = roundUpTo(this.endAmount / this.endUsdExchangeRate, 0.1f);
			this.startAmountUsd = roundUpTo(this.startAmount / ExchangeRateStats.currentUsdExchangeRate, 0.1f);
			this.profitUsd = this.endAmountUsd - this.startAmountUsd;
			this.profitRateUsd = 100.0f * this.profitUsd / this.startAmountUsd;
			this.profitPerDayUsd = this.profitUsd / this.period;
		}

		this.accountStatement = accountStatement.toArray(new AccountStatementRecord[accountStatement.size()]);
	}

	private float roundUpTo(float amount, float base) {
		return (float) (base * (Math.floor(amount / base)));
	}
}
