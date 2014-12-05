package com.dudko.highwave.deposit.deposits;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class DearDeposit extends Deposit {
	public DearDeposit() {
		bank = BankFactory.GetBank(BankCode.VTBBank);
		name = DepositNames.MSG_018_Dear;
		url = "http://www.vtb-bank.by/personal/deposit/dear/";
		currency = Currency.BYR;
		interestRate = 30.0f;
	}

	@Override
	public DepositAccount calculateDeposit(long amount, int period) {
		long minOpenAmount = 20000000;
		if (amount < minOpenAmount) {
			return null;
		}

		int depositTerm = 395;
		int term = Math.min(period, depositTerm);
		LocalDate currentDate = MinskLocalDate.now();
		LocalDate endDate = currentDate.plusDays(term);

		float _interestRate = interestRate(term);
		float _amount = amount;

		DepositAccount account = new DepositAccount(this);
		account.addRecord(currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		LocalDate previousDate = currentDate;
		currentDate = currentDate.plusMonths(1);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			_amount = calculatePeriod(_amount, _interestRate, previousDate, currentDate);
			account.addRecord(currentDate, _amount, _interestRate, RecordDescriptions.MSG_001_Capitalization);

			previousDate = currentDate;
			currentDate = currentDate.plusMonths(1);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period > 0) {
			_amount = calculatePeriod(_amount, _interestRate, _period);
			account.addRecord(endDate, _amount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		if (term == depositTerm) {
			account.addRecord(endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			account.addRecord(endDate, _amount, _interestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		account.fillData();
		return account;
	}

	private float interestRate(int term) {
		float lowInterestRate = 0.1f;

		LocalDate currentDate = MinskLocalDate.now();
		LocalDate endDate = currentDate.plusDays(term);
		int months = Months.monthsBetween(currentDate, endDate).getMonths();

		return months < 2 ? lowInterestRate : interestRate;
	}
}