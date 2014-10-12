package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class StartDepositBYR extends StartDeposit {
	public StartDepositBYR() {
		super();

		currency = Currency.BYR;
		interestRate = 29.0f;

		minOpenAmount = 1000000.0f;
		minDepositAmount = 3000000.0f;
	}
}