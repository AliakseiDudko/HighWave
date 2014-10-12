package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class StartDepositCUR extends StartDeposit {
	public StartDepositCUR() {
		super();

		currency = Currency.CUR;
		interestRate = 4.5f;

		minOpenAmount = 1.0f;
		minDepositAmount = 1000.0f;
	}
}