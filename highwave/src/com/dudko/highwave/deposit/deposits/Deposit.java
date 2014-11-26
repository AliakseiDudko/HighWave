package com.dudko.highwave.deposit.deposits;

import org.joda.time.*;

import com.dudko.highwave.bank.banks.Bank;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public abstract class Deposit {
	public Bank bank;
	public DepositNames name;
	public String url;
	public Currency currency;
	public float interestRate;

	public abstract DepositAccount calculateDeposit(float amount, int period);

	protected float calculatePeriod(float amount, float interestRate, LocalDate startDate, LocalDate endDate) {
		int period = Days.daysBetween(startDate, endDate).getDays();
		return calculatePeriod(amount, interestRate, period);
	}

	protected float calculatePeriod(float amount, float interestRate, int period) {
		return amount * (1 + interestRate * period / (100.0f * 365));
	}

	protected float calculatePeriod360(float amount, float interestRate, LocalDate startDate, LocalDate endDate) {
		int period = Days.daysBetween(startDate, endDate).getDays();
		return calculatePeriod360(amount, interestRate, period);
	}

	protected float calculatePeriod360(float amount, float interestRate, int period) {
		return amount * (1 + interestRate * period / (100.0f * 360));
	}
}
