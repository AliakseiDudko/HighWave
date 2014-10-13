package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public class AnnualDeposit extends Deposit {
	private int depositTerm = 365;
	private float minOpenAmount = 1000000f;
	private float lowInterestRate = 1.0f;

	public AnnualDeposit() {
		bank = BankFactory.GetBank(BankCode.BelGazPromBank);
		name = DepositNames.MSG_000_Annual;
		url = "http://belgazprombank.by/personal_banking/vkladi_depoziti/v_nacional_noj_valjute/vklad_godovoj/";
		currency = Currency.BYR;
		interestRate = 28.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		period = Math.min(period, depositTerm);
		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(period);
		float _interestRate = interestRate(period);
		float _amount = amount;

		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Depost);

		DateTime previousDate = currentDate;
		currentDate = currentDate.plusMonths(1);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			int _period = Days.daysBetween(previousDate, currentDate).getDays();
			_amount = calculatePeriod(_amount, _interestRate, _period);
			addRecord(list, currentDate, _amount, _interestRate, RecordDescriptions.MSG_001_Capitalization);

			previousDate = currentDate;
			currentDate = currentDate.plusMonths(1);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period > 0) {
			_amount = calculatePeriod(_amount, _interestRate, _period);
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);

		return new DepositAccount(this, list);
	}

	private float interestRate(int _period) {
		if (_period <= 30) {
			return lowInterestRate;
		} else if (_period <= 90) {
			return interestRate - 2.0f;
		} else {
			return interestRate;
		}
	}
}
