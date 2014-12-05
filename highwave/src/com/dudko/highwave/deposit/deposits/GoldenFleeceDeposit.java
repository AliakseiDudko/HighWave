package com.dudko.highwave.deposit.deposits;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class GoldenFleeceDeposit extends Deposit {
	public GoldenFleeceDeposit() {
		bank = BankFactory.GetBank(BankCode.IdeaBank);
		name = DepositNames.MSG_015_GoldenFleece;
		url = "http://ideabank.by/vklad-zolotoe-runo";
		currency = Currency.BYR;
		interestRate = 30.0f;
	}

	@Override
	public DepositAccount calculateDeposit(long amount, int period) {
		long minOpenAmount = 200000;
		if (amount < minOpenAmount) {
			return null;
		}

		LocalDate currentDate = MinskLocalDate.now();
		int depositTerm = Days.daysBetween(currentDate, currentDate.plusMonths(18)).getDays();
		int term = Math.min(depositTerm, period);
		LocalDate twoMonthsDate = currentDate.plusMonths(2);
		int twoMonthsTerm = Days.daysBetween(currentDate, twoMonthsDate).getDays();
		LocalDate endDate = currentDate.plusDays(term);

		float _amount = amount;
		float lowInterestRate1 = 1.5f;
		float lowInterestRate2 = 17.0f;
		float endDateInterestRate;

		DepositAccount account = new DepositAccount(this);
		account.addRecord(currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		if (term < twoMonthsTerm) {
			_amount += calculatePeriod(amount, lowInterestRate1, term) - amount;
			account.addRecord(endDate, _amount, lowInterestRate1, RecordDescriptions.MSG_002_Accrual_Of_Interest);

			endDateInterestRate = lowInterestRate1;
		} else {
			_amount += calculatePeriod(amount, interestRate, twoMonthsTerm) - amount;
			account.addRecord(twoMonthsDate, _amount, interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);

			if (twoMonthsTerm < term) {
				int _period = term - twoMonthsTerm;
				_amount += calculatePeriod(amount, lowInterestRate2, _period) - amount;
				account.addRecord(endDate, _amount, lowInterestRate2, RecordDescriptions.MSG_002_Accrual_Of_Interest);

				endDateInterestRate = lowInterestRate2;
			} else {
				endDateInterestRate = interestRate;
			}
		}

		if (term == depositTerm) {
			account.addRecord(endDate, _amount, endDateInterestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			account.addRecord(endDate, _amount, endDateInterestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		account.fillData();
		return account;
	}
}