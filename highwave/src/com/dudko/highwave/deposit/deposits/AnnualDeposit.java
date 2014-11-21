package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public class AnnualDeposit extends Deposit {
	public AnnualDeposit() {
		bank = BankFactory.GetBank(BankCode.BelGazPromBank);
		name = DepositNames.MSG_000_Annual;
		url = "http://belgazprombank.by/personal_banking/vkladi_depoziti/v_nacional_noj_valjute/vklad_godovoj/";
		currency = Currency.BYR;
		interestRate = 28.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		float minOpenAmount = 1000000f;
		if (amount < minOpenAmount) {
			return null;
		}

		int depositTerm = 365;
		int term = Math.min(period, depositTerm);

		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(term);

		float _interestRate = interestRate(term);
		float _amount = amount;

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();
		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		DateTime previousDate = currentDate;
		currentDate = currentDate.plusMonths(1);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			_amount = calculatePeriod(_amount, _interestRate, previousDate, currentDate);
			addRecord(list, currentDate, _amount, _interestRate, RecordDescriptions.MSG_001_Capitalization);

			previousDate = currentDate;
			currentDate = currentDate.plusMonths(1);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period > 0) {
			_amount = calculatePeriod(_amount, _interestRate, _period);
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		if (term < depositTerm) {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		} else {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		}

		return new DepositAccount(this, list);
	}

	private float interestRate(int term) {
		float lowInterestRate = 1.0f;

		if (term <= 30) {
			return lowInterestRate;
		} else if (term <= 90) {
			return interestRate - 2.0f;
		} else {
			return interestRate;
		}
	}
}
