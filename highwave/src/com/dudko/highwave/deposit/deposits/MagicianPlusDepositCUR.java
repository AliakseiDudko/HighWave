package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.Currency;

public class MagicianPlusDepositCUR extends MagicianPlusDeposit {
	public MagicianPlusDepositCUR(int depositTermMonths) {
		super(depositTermMonths);

		currency = Currency.CUR;
		interestRate = 5.7f;

		minOpenAmount = 500;
		minDepositAmount = 500.0f;
		bonusInterest = 0.5f;
	}
}
