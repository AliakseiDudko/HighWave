package com.dudko.highwave.deposit.deposits;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public abstract class EarlierMoreDeposit extends Deposit {
	protected int depositTerm;
	protected float lowInterestRate = 0.1f;

	private int capitalizationPeriod = 30;

	public EarlierMoreDeposit() {
		bank = BankFactory.GetBank(BankCode.HomeCreditBank);
		currency = Currency.BYR;
	}

	@Override
	public DepositAccount calculateDeposit(long amount, int period) {
		long minOpenAmount = 1000000;
		if (amount < minOpenAmount) {
			return null;
		}

		int term = Math.min(period, depositTerm);
		LocalDate currentDate = MinskLocalDate.now();
		LocalDate endDate = currentDate.plusDays(term);

		float _interestRate = interestRate(term);
		float depositAmount = amount;

		DepositAccount account = new DepositAccount(this);
		account.addRecord(currentDate, depositAmount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		LocalDate previousDate = currentDate;
		currentDate = currentDate.plusDays(capitalizationPeriod);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			depositAmount = calculatePeriod(depositAmount, _interestRate, capitalizationPeriod);
			account.addRecord(currentDate, depositAmount, _interestRate, RecordDescriptions.MSG_001_Capitalization);

			previousDate = currentDate;
			currentDate = currentDate.plusDays(capitalizationPeriod);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period > 0) {
			depositAmount = calculatePeriod(depositAmount, _interestRate, _period);
			account.addRecord(endDate, depositAmount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		if (period == depositTerm) {
			account.addRecord(endDate, depositAmount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else if (period <= depositTerm) {
			account.addRecord(endDate, depositAmount, _interestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		} else {
			_period = period - depositTerm;
			currentDate = endDate.plusDays(_period);
			depositAmount = calculatePeriod(depositAmount, lowInterestRate, _period);
			account.addRecord(currentDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
			account.addRecord(currentDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		}

		account.fillData();
		return account;
	}

	protected abstract float interestRate(int _period);
}