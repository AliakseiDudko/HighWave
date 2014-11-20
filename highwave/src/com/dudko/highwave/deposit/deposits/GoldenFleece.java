package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public class GoldenFleece extends Deposit {
	public GoldenFleece() {
		bank = BankFactory.GetBank(BankCode.IdeaBank);
		name = DepositNames.MSG_015_GoldenFleece;
		url = "http://ideabank.by/vklad-zolotoe-runo";
		currency = Currency.BYR;
		interestRate = 30.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		float minOpenAmount = 200000.0f;
		if (amount < minOpenAmount) {
			return null;
		}

		DateTime currentDate = DateTime.now();
		int depositTerm = Days.daysBetween(currentDate, currentDate.plusMonths(18)).getDays();
		int term = Math.min(depositTerm, period);
		DateTime twoMonthsDate = currentDate.plusMonths(2);
		int twoMonthsTerm = Days.daysBetween(currentDate, twoMonthsDate).getDays();
		DateTime endDate = currentDate.plusDays(term);

		float _amount = amount;
		float lowInterestRate1 = 1.5f;
		float lowInterestRate2 = 17.0f;
		float endDateInterestRate;

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();
		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		if (term < twoMonthsTerm) {
			_amount += calculatePeriod(amount, lowInterestRate1, term) - amount;
			addRecord(list, endDate, _amount, lowInterestRate1, RecordDescriptions.MSG_002_Accrual_Of_Interest);

			endDateInterestRate = lowInterestRate1;
		} else {
			_amount += calculatePeriod(amount, interestRate, twoMonthsTerm) - amount;
			addRecord(list, twoMonthsDate, _amount, interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);

			if (twoMonthsTerm < term) {
				int _period = term - twoMonthsTerm;
				_amount += calculatePeriod(amount, lowInterestRate2, _period) - amount;
				addRecord(list, endDate, _amount, lowInterestRate2, RecordDescriptions.MSG_002_Accrual_Of_Interest);

				endDateInterestRate = lowInterestRate2;
			} else {
				endDateInterestRate = interestRate;
			}
		}

		if (term == depositTerm) {
			addRecord(list, endDate, _amount, endDateInterestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			addRecord(list, endDate, _amount, endDateInterestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		return new DepositAccount(this, list);
	}
}
