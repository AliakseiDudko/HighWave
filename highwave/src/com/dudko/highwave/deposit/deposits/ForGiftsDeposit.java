package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class ForGiftsDeposit extends Deposit {
	public ForGiftsDeposit() {
		bank = BankFactory.GetBank(BankCode.IdeaBank);
		name = DepositNames.MSG_016_ForGifts;
		url = "http://ideabank.by/vklad-na-podarki";
		currency = Currency.BYR;
		interestRate = 29.5f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		float minOpenAmount = 200000.0f;
		if (amount < minOpenAmount) {
			return null;
		}

		LocalDate currentDate = MinskLocalDate.now();
		int depositTerm = Days.daysBetween(currentDate, currentDate.plusMonths(18)).getDays();
		int term = Math.min(depositTerm, period);
		LocalDate endDate = currentDate.plusDays(term);

		float _amount = amount;

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();
		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		LocalDate previousDate = currentDate;
		currentDate = currentDate.plusMonths(1);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			float _interestRate = interestRate(currentDate, term);
			_amount += calculatePeriod(amount, _interestRate, previousDate, currentDate) - amount;
			addRecord(list, currentDate, _amount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);

			previousDate = currentDate;
			currentDate = currentDate.plusMonths(1);
		}

		float _interestRate = interestRate(endDate, term);
		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period > 0) {
			_amount += calculatePeriod(amount, _interestRate, _period) - amount;
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		if (term == depositTerm) {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		return new DepositAccount(this, list);
	}

	private float interestRate(LocalDate currentDate, int term) {
		LocalDate today = MinskLocalDate.now();
		int monthTerm = Days.daysBetween(today, today.plusMonths(1)).getDays();
		int currentTerm = Days.daysBetween(today, currentDate).getDays();

		float lowInterestRate1 = 1.5f;
		float lowInterestRate2 = 25.0f;

		if (term < monthTerm) {
			return lowInterestRate1;
		}

		if (currentTerm <= monthTerm) {
			return interestRate;
		} else {
			return lowInterestRate2;
		}
	}
}
