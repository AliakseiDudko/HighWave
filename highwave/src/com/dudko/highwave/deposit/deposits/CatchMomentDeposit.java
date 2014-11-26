package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class CatchMomentDeposit extends Deposit {
	public CatchMomentDeposit() {
		bank = BankFactory.GetBank(BankCode.VTBBank);
		name = DepositNames.MSG_010_CatchMoment;
		url = "http://vtb-bank.by/personal/deposit/lovi_moment/";
		currency = Currency.CUR;
		interestRate = 6.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		float minOpenAmount = 1000.0f;
		if (amount < minOpenAmount) {
			return null;
		}

		int depositTerm = 732;
		int term = Math.min(period, depositTerm);
		LocalDate currentDate = MinskLocalDate.now();
		LocalDate endDate = currentDate.plusDays(term);

		float _interestRate = interestRate(term);
		float _amount = amount;

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();
		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		LocalDate previousDate = currentDate;
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

		if (term == depositTerm) {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		return new DepositAccount(this, list);
	}

	private float interestRate(int term) {
		float lowInterestRate = 0.1f;

		LocalDate currentDate = MinskLocalDate.now();
		LocalDate endDate = currentDate.plusDays(term);
		int months = Months.monthsBetween(currentDate, endDate).getMonths();

		return months < 6 ? lowInterestRate : interestRate;
	}
}