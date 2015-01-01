package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class SkarbonkaDepositCUR extends SkarbonkaDeposit {
	public SkarbonkaDepositCUR() {
		super();

		currency = Currency.CUR;
		interestRate = 6.5f;
		minOpenAmount = 100;
	}
}