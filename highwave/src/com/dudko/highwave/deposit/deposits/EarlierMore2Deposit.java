package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.globalize.*;

public class EarlierMore2Deposit extends EarlierMoreDeposit {
	public EarlierMore2Deposit() {
		super();

		depositTerm = 270;

		name = DepositNames.MSG_001_EarlierMore2;
		url = "http://homecredit.by/loans_and_services/Ranshe_Bolshe_2/";
		interestRate = 27.0f;
	}

	protected float interestRate(int _period) {
		if (_period <= 90) {
			return lowInterestRate;
		} else if (_period <= 105) {
			return 29.0f;
		} else if (_period <= 120) {
			return 27.0f;
		} else if (_period <= 135) {
			return 25.0f;
		} else if (_period <= 150) {
			return 23.0f;
		} else if (_period <= 165) {
			return 21.0f;
		} else if (_period <= 180) {
			return 19.0f;
		} else if (_period <= depositTerm) {
			return 17.0f;
		}

		return lowInterestRate;
	}
}