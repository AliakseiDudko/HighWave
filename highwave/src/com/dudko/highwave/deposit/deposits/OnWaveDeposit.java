package com.dudko.highwave.deposit.deposits;

import java.util.*;

import org.joda.time.DateTime;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.deposit.Currency;
import com.dudko.highwave.globalize.RecordDescriptions;

public class OnWaveDeposit extends Deposit {
	private int depositTerm = 10;
	private float minOpenAmount = 1000000f;

	public OnWaveDeposit() {
		bank = BankFactory.GetBank(BankCode.HomeCreditBank);
		name = "На волне";
		url = "http://www.homecredit.by/loans_and_services/na_volne/";
		currency = Currency.BYR;
		interestRate = 20.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount || period < depositTerm) {
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

		addRecord(list, previousDate, _amount, interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);

		return new DepositAccount(this, list);
	}
}