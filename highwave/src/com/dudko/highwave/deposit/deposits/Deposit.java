package com.dudko.highwave.deposit.deposits;

import java.util.List;

import org.joda.time.DateTime;

import com.dudko.highwave.bank.banks.Bank;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.DepositNames;
import com.dudko.highwave.globalize.RecordDescriptions;

public abstract class Deposit {
	public Bank bank;
	public DepositNames name;
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

	public void addRecord(List<AccountStatementRecord> list, DateTime date, float amount, float interestRate, RecordDescriptions description) {
		addRecord(list, date, amount, interestRate, description, false);
	}

	public void addRecord(List<AccountStatementRecord> list, DateTime date, float amount, float interestRate,
			RecordDescriptions description, Boolean isLast) {
		AccountStatementRecord record = new AccountStatementRecord(date, amount, interestRate, description).setIsLast(isLast);
		list.add(record);
	}
}
