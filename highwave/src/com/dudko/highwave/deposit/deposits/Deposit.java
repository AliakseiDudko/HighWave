package com.dudko.highwave.deposit.deposits;

import java.util.List;

import org.joda.time.DateTime;

import com.dudko.highwave.bank.banks.Bank;
import com.dudko.highwave.deposit.*;

public abstract class Deposit {
	public int id;
	public Bank bank;
	public String name;
	public String url;
	public Currency currency;
	public float interestRate;

	public abstract DepositAccount calculateDeposit(float amount, int period);

	public float calculatePeriod(float amount, float interestRate, int period) {
		return amount * (1 + interestRate * period / (100.0f * 365));
	}

	public float calculatePeriod360(float amount, float interestRate, int period) {
		return amount * (1 + interestRate * period / (100.0f * 360));
	}

	public void addRecord(List<AccountStatementRecord> list, DateTime date, float amount, float interestRate,
			String description) {
		addAccountStatementRecord(list, date, amount, interestRate, description, false);
	}

	public void addAccountStatementRecord(List<AccountStatementRecord> list, DateTime date, float amount, float interestRate,
			String description, Boolean isLast) {
		AccountStatementRecord record = new AccountStatementRecord(date, amount, interestRate, description).setIsLast(isLast);
		list.add(record);
	}
}
