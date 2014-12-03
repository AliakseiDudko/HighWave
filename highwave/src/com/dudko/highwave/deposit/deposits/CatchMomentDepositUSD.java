package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class CatchMomentDepositUSD extends CatchMomentDeposit {
	public CatchMomentDepositUSD() {
		super();

		currency = Currency.USD;
		interestRate = 4.5f;
	}
}