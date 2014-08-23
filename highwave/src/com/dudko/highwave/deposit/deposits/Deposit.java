package com.dudko.highwave.deposit.deposits;

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
}
