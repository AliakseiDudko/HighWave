package com.dudko.highwave.deposit.deposits;

import java.util.*;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public class PrudentDeposit extends Deposit {
	public PrudentDeposit() {
		bank = BankFactory.GetBank(BankCode.TechnoBank);
		name = DepositNames.MSG_013_Prudent;
		url = "http://tb.by/private/deposits/#valuta";
		currency = com.dudko.highwave.deposit.Currency.CUR;
		interestRate = 5.2f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		float minOpenAmount = 100.0f;
		if (amount < minOpenAmount) {
			return null;
		}

		int depositTerm = 720;
		int term = Math.min(period, depositTerm);
		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(term);

		float _amount = amount;

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();
		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		Set<Integer> setOfCapitalization = new TreeSet<Integer>();
		DateTime futureDate = currentDate.plusMonths(1);
		while (futureDate.isBefore(endDate) || futureDate.isEqual(endDate)) {
			int _period = Days.daysBetween(currentDate, futureDate).getDays();
			setOfCapitalization.add(_period);

			futureDate = futureDate.plusMonths(1);
		}

		Set<Integer> setOfDays = new TreeSet<Integer>();
		setOfDays.addAll(Arrays.asList(0, 180, 370, 550, 720, period));
		setOfDays.addAll(setOfCapitalization);
		Integer[] days = setOfDays.toArray(new Integer[0]);

		for (int i = 0; days[i] < term; i++) {
			int day = days[i + 1];
			int _period = day - days[i];

			float _interestRate = interestRate(day, period);
			_amount = calculatePeriod(_amount, _interestRate, _period);

			currentDate = currentDate.plusDays(_period);
			if (setOfCapitalization.contains(day)) {
				addRecord(list, currentDate, _amount, _interestRate, RecordDescriptions.MSG_001_Capitalization);
			} else {
				addRecord(list, currentDate, _amount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
			}
		}

		float _interestRate = interestRate(term, period);
		if (term == depositTerm) {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		return new DepositAccount(this, list);
	}

	private float interestRate(int day, int period) {
		float lowInterestRate = 2.5f;
		if (period <= 180) {
			return lowInterestRate;
		}

		if (day <= 180) {
			return 4.0f;
		} else if (day <= 370) {
			return 4.5f;
		} else if (day <= 550) {
			return 5.0f;
		} else if (day <= 720) {
			return 6.0f;
		} else {
			return lowInterestRate;
		}
	}
}
