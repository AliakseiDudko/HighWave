package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class SavingDeposit extends Deposit {
	public SavingDeposit() {
		bank = BankFactory.GetBank(BankCode.ParitetBank);
		name = DepositNames.MSG_011_Saving;
		url = "http://www.paritetbank.by/services/private/deposit/banks_deposit/vklad_v_belrub_sberegatelnyi/";
		currency = Currency.BYR;
		interestRate = 27.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		LocalDate currentDate = MinskLocalDate.now();
		LocalDate maxEndDate = currentDate.plusYears(3);

		int maxPeriod = Days.daysBetween(currentDate, maxEndDate).getDays();
		int term = Math.min(period, maxPeriod);

		LocalDate endDate = currentDate.plusDays(term);
		int months = Months.monthsBetween(currentDate, endDate).getMonths();

		float _interestRate = interestRate(months);
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

		if (endDate.equals(maxEndDate)) {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		return new DepositAccount(this, list);
	}

	private float interestRate(int months) {
		if (months < 1) {
			return 0.1f;
		} else if (1 <= months && months < 3) {
			return 13.0f;
		} else {
			return interestRate;
		}
	}
}