package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class MagicianPlusDepositBYR extends MagicianPlusDeposit {
	public MagicianPlusDepositBYR(int depositTermMonths) {
		super(depositTermMonths);

		currency = Currency.BYR;
		interestRate = 20.0f;

		minOpenAmount = 5000000;
		minDepositAmount = 5000000.0f;
		bonusInterest = 4.0f;
	}
}
