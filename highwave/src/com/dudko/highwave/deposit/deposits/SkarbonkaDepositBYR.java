package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class SkarbonkaDepositBYR extends SkarbonkaDeposit {
	public SkarbonkaDepositBYR() {
		super();

		currency = Currency.BYR;
		interestRate = 30.0f;

		minOpenAmount = 200000f;
		lowInterestRate = 0.1f;
	}
}