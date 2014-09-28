package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.RecordDescriptions;

public class RapidProfitDeposit extends Deposit {
	private int depositTerm = 10;
	private float minOpenAmount = 1000000f;
	private float lowInterestRate = 0.01f;

	public RapidProfitDeposit() {
		id = 4;
		bank = BankFactory.GetBank(BankCode.BTABank);
		name = "Стремительный доход";
		url = "http://www.btabank.by/ru/block/1257/";
		currency = Currency.BYR;
		interestRate = 23.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		int term = Math.min(period, 30);
		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(term);
		float _amount = amount;

		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Depost);

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
			addRecord(list, endDate, _amount, lowInterestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		}

		return new DepositAccount(this, list);
	}
}
