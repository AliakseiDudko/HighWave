package com.dudko.highwave.deposit.deposits;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class MTSquirrelsDeposit extends Deposit {
	private float lowInterestRate = 1.0f;
	private int fixPeriodMonths;

	public MTSquirrelsDeposit(DepositNames name, int fixPeriodMonths) {
		bank = BankFactory.GetBank(BankCode.MTBank);
		this.name = name;
		url = "http://www.mtbank.by/private/deposits/br/mtbelki";
		currency = Currency.BYR;
		interestRate = interestRate(fixPeriodMonths);
		this.fixPeriodMonths = fixPeriodMonths;
	}

	@Override
	public DepositAccount calculateDeposit(long amount, int period) {
		long minOpenAmount = 500000;
		if (amount < minOpenAmount) {
			return null;
		}

		LocalDate currentDate = MinskLocalDate.now();
		int term = Math.min(Days.daysBetween(currentDate, currentDate.plusMonths(18)).getDays(), period);
		LocalDate endDate = currentDate.plusDays(term);
		LocalDate startFixPeriodDate = currentDate;
		LocalDate endFixPeriodDate = currentDate.plusMonths(fixPeriodMonths);

		float bonusInterest = 0.5f;
		float depositAmount = amount;

		DepositAccount account = new DepositAccount(this);
		account.addRecord(currentDate, depositAmount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		int months = 0;
		LocalDate previousDate = currentDate;
		currentDate = currentDate.plusMonths(1);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			float _interestRate = endFixPeriodDate.isBefore(endDate) || endFixPeriodDate.isEqual(endDate) ? interestRate : lowInterestRate;
			for (int i = 0; i < fixPeriodMonths && currentDate.isBefore(endDate) || currentDate.isEqual(endDate); i++) {
				depositAmount = calculatePeriod(depositAmount, _interestRate, previousDate, currentDate);
				account.addRecord(currentDate, depositAmount, _interestRate, RecordDescriptions.MSG_001_Capitalization);

				months++;
				previousDate = currentDate;
				currentDate = currentDate.plusMonths(1);
			}

			int _period = Days.daysBetween(previousDate, endDate).getDays();
			if (_period > 0 && months != 0 && months % fixPeriodMonths == 0) {
				depositAmount = calculatePeriod(depositAmount, bonusInterest, startFixPeriodDate, endFixPeriodDate);
				account.addRecord(previousDate, depositAmount, bonusInterest, RecordDescriptions.MSG_007_Bonus);
			}

			startFixPeriodDate = endFixPeriodDate;
			endFixPeriodDate = endFixPeriodDate.plusMonths(fixPeriodMonths);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period > 0) {
			depositAmount = calculatePeriod(depositAmount, lowInterestRate, _period);
			account.addRecord(endDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		if (months % fixPeriodMonths == 0 && _period == 0) {
			account.addRecord(endDate, depositAmount, interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			account.addRecord(endDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		account.fillData();
		return account;
	}

	private float interestRate(int _fixPeriodMonths) {
		switch (_fixPeriodMonths) {
		case 1:
			return 26.0f;
		case 2:
			return 27.0f;
		case 3:
			return 28.0f;
		}

		return lowInterestRate;
	}
}