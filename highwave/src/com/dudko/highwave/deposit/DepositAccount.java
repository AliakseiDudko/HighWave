package com.dudko.highwave.deposit;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.deposit.deposits.Deposit;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class DepositAccount {
	public Deposit deposit;
	public List<AccountStatementRecord> accountStatement;

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

	public DepositAccount(Deposit deposit) {
		this.deposit = deposit;
		this.accountStatement = new ArrayList<AccountStatementRecord>();
	}

	public void addRecord(LocalDate date, float amount, float interestRate, RecordDescriptions description) {
		addRecord(date, amount, interestRate, description, false);
	}

	public void addRecord(LocalDate date, float amount, float interestRate, RecordDescriptions description, Boolean isLast) {
		AccountStatementRecord record = new AccountStatementRecord(date, amount, interestRate, description).setIsLast(isLast);
		accountStatement.add(record);
	}

	public void fillData() {
		if (accountStatement.isEmpty()) {
			return;
		}

		AccountStatementRecord firstRecord = accountStatement.get(0);
		AccountStatementRecord lastRecord = getLastRecord();
		startDate = firstRecord.date;
		endDate = lastRecord.date;
		period = Days.daysBetween(firstRecord.GetOriginalDate(), lastRecord.GetOriginalDate()).getDays();

		startAmount = roundUpTo(firstRecord.amount, deposit.currency);
		endAmount = roundUpTo(lastRecord.amount, deposit.currency);
		profit = endAmount - startAmount;
		profitRate = 100.0f * profit / startAmount;
		profitPerDay = profit / period;

		float usdDelta = 120.0f;
		endUsdExchangeRate = roundUpTo(ExchangeRateStats.currentUsdExchangeRate + period * ExchangeRateStats.dailyUsdExchangeRateDelta + usdDelta, 10);
		endAmountUsd = roundUpTo(endAmount / endUsdExchangeRate, 0.1f);
		startAmountUsd = roundUpTo(startAmount / (ExchangeRateStats.currentUsdExchangeRate + usdDelta), 0.1f);
		profitUsd = endAmountUsd - startAmountUsd;
		profitRateUsd = 100.0f * profitUsd / startAmountUsd;
		profitPerDayUsd = profitUsd / period;
	}

	private AccountStatementRecord getLastRecord() {
		AccountStatementRecord lastRecord = accountStatement.get(accountStatement.size() - 1);

		for (AccountStatementRecord record : accountStatement) {
			if (record.isLast) {
				lastRecord = record;
				break;
			}
		}

		return lastRecord;
	}

	private float roundUpTo(float amount, Currency currency) {
		float base = currency == Currency.BYR ? 50 : 1;
		return roundUpTo(amount, base);
	}

	private float roundUpTo(float amount, float base) {
		return (float) (base * (Math.floor(amount / base)));
	}
}