package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class StartDepositCUR extends StartDeposit {
	public StartDepositCUR() {
		super();

		currency = Currency.CUR;
		interestRate = 5.25f;

		minOpenAmount = 1;
		minDepositAmount = 1000.0f;
	}
}