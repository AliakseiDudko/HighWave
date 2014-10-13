package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class SkarbonkaDepositCUR extends SkarbonkaDeposit {
	public SkarbonkaDepositCUR() {
		super();

		currency = Currency.CUR;
		interestRate = 5.0f;

		minOpenAmount = 100f;
		lowInterestRate = 0.1f;
	}
}