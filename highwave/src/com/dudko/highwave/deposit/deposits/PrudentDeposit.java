package com.dudko.highwave.deposit.deposits;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class PrudentDeposit extends Deposit {
	public PrudentDeposit() {
		bank = BankFactory.GetBank(BankCode.TechnoBank);
		name = DepositNames.MSG_013_Prudent;
		url = "http://tb.by/private/deposits/#valuta";
		currency = Currency.CUR;
		interestRate = 5.5f;
	}

	@Override
	public DepositAccount calculateDeposit(long amount, int period) {
		long minOpenAmount = 100;
		if (amount < minOpenAmount) {
			return null;
		}

		int depositTerm = 720;
		int term = Math.min(period, depositTerm);
		LocalDate currentDate = MinskLocalDate.now();
		LocalDate endDate = currentDate.plusDays(term);

		float _amount = amount;

		DepositAccount account = new DepositAccount(this);
		account.addRecord(currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		Set<Integer> setOfCapitalization = new TreeSet<Integer>();
		LocalDate futureDate = currentDate.plusMonths(1);
		while (futureDate.isBefore(endDate) || futureDate.isEqual(endDate)) {
			int _period = Days.daysBetween(currentDate, futureDate).getDays();
			setOfCapitalization.add(_period);

			futureDate = futureDate.plusMonths(1);
		}

		Set<Integer> setOfDays = new TreeSet<Integer>();
		setOfDays.addAll(Arrays.asList(0, 180, 370, 550, 720, term));
		setOfDays.addAll(setOfCapitalization);
		Integer[] days = setOfDays.toArray(new Integer[0]);

		for (int i = 0; days[i] < term; i++) {
			int day = days[i + 1];
			int _period = day - days[i];

			float _interestRate = interestRate(day, term);
			_amount = calculatePeriod(_amount, _interestRate, _period);

			currentDate = currentDate.plusDays(_period);
			if (setOfCapitalization.contains(day)) {
				account.addRecord(currentDate, _amount, _interestRate, RecordDescriptions.MSG_001_Capitalization);
			} else {
				account.addRecord(currentDate, _amount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
			}
		}

		float _interestRate = interestRate(term, term);
		if (term == depositTerm) {
			account.addRecord(endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			account.addRecord(endDate, _amount, _interestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		account.fillData();
		return account;
	}

	private float interestRate(int day, int term) {
		float lowInterestRate = 2.5f;
		if (term <= 180) {
			return lowInterestRate;
		}

		if (day <= 180) {
			return 4.0f;
		} else if (day <= 370) {
			return 5.0f;
		} else if (day <= 550) {
			return 5.5f;
		} else if (day <= 720) {
			return 6.0f;
		} else {
			return lowInterestRate;
		}
	}
}