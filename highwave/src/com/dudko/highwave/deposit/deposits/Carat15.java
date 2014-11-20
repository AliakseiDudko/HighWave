package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public class Carat15 extends Deposit {
	public Carat15() {
		bank = BankFactory.GetBank(BankCode.HomeCreditBank);
		name = DepositNames.MSG_014_Carat15;
		url = "http://homecredit.by/loans_and_services/15_karat/";
		currency = Currency.BYR;
		interestRate = 29.5f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		float lowInterestRate = 0.1f;
		float minOpenAmount = 1000000.0f;
		if (amount < minOpenAmount) {
			return null;
		}

		int depositTerm = 15;
		int term = Math.min(period, 30);
		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(term);

		float _amount = amount;

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();
		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		DateTime previousDate = currentDate;
		currentDate = currentDate.plusDays(depositTerm);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			_amount = calculatePeriod(_amount, interestRate, depositTerm);
			addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_001_Capitalization);

			previousDate = currentDate;
			currentDate = currentDate.plusDays(depositTerm);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period == 0) {
			addRecord(list, endDate, _amount, interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			_amount = calculatePeriod(_amount, lowInterestRate, _period);
			addRecord(list, endDate, _amount, lowInterestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
			addRecord(list, endDate, _amount, lowInterestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		}

		return new DepositAccount(this, list);
	}
}
