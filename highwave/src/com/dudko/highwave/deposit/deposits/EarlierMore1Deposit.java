package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.globalize.*;

public class EarlierMore1Deposit extends EarlierMoreDeposit {
	public EarlierMore1Deposit() {
		super();

		depositTerm = 90;

		name = DepositNames.MSG_001_EarlierMore1;
		url = "http://homecredit.by/loans_and_services/ranshe_bolshe/";
		interestRate = 28.0f;
	}

	protected float interestRate(int _period) {
		if (_period <= 30) {
			return lowInterestRate;
		} else if (_period <= 40) {
			return 32.0f;
		} else if (_period <= 50) {
			return 30.0f;
		} else if (_period <= 60) {
			return 29.0f;
		} else if (_period <= 70) {
			return 28.0f;
		} else if (_period <= 80) {
			return 26.0f;
		} else if (_period <= 90) {
			return 23.0f;
		}

		return lowInterestRate;
	}
}