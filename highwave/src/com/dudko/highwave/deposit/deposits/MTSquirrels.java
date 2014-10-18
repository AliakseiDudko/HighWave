package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public class MTSquirrels extends Deposit {
	private float minOpenAmount = 500000f;
	private float lowInterestRate = 1.0f;
	private int fixPeriodMonths = 2;

	public MTSquirrels() {
		bank = BankFactory.GetBank(BankCode.MTBank);
		name = DepositNames.MSG_003_MTSquirrels;
		url = "http://www.mtbank.by/private/deposits/br/mtbelki";
		currency = Currency.BYR;
		interestRate = 28.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		DateTime currentDate = DateTime.now();
		int term = Math.min(Days.daysBetween(currentDate, currentDate.plusMonths(18)).getDays(), period);
		DateTime endDate = currentDate.plusDays(term);
		float depositAmount = amount;

		addRecord(list, currentDate, depositAmount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		DateTime previousDate = currentDate;
		DateTime fixPeriodDate = currentDate.plusMonths(fixPeriodMonths);
		currentDate = currentDate.plusMonths(1);

		int months = 0;
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			float _interestRate = fixPeriodDate.isBefore(endDate) || fixPeriodDate.isEqual(endDate) ? interestRate : lowInterestRate;
			for (int i = 0; i < fixPeriodMonths && currentDate.isBefore(endDate) || currentDate.isEqual(endDate); i++) {
				int _period = Days.daysBetween(previousDate, currentDate).getDays();
				depositAmount = calculatePeriod(depositAmount, _interestRate, _period);
				addRecord(list, currentDate, depositAmount, _interestRate, RecordDescriptions.MSG_001_Capitalization);

				months++;
				previousDate = currentDate;
				currentDate = currentDate.plusMonths(1);
			}

			int _period = Days.daysBetween(previousDate, endDate).getDays();
			if (_period > 0 && months != 0 && months % fixPeriodMonths == 0) {
				depositAmount *= 1.005f;
				addRecord(list, previousDate, depositAmount, interestRate, RecordDescriptions.MSG_004_Bonus_05_Percent);
			}

			fixPeriodDate = fixPeriodDate.plusMonths(fixPeriodMonths);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period != 0) {
			depositAmount = calculatePeriod(depositAmount, lowInterestRate, _period);
			addRecord(list, endDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		if (months % fixPeriodMonths == 0 && _period == 0) {
			addRecord(list, endDate, depositAmount, interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			addRecord(list, endDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		return new DepositAccount(this, list);
	}
}
