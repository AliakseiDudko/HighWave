package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class CatchMomentDepositEUR extends CatchMomentDeposit {
	public CatchMomentDepositEUR() {
		super();

		currency = Currency.EUR;
		interestRate = 4.5f;
	}
}