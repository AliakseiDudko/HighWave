package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public abstract class SkarbonkaDeposit extends Deposit {
	protected float minOpenAmount;
	protected float lowInterestRate;

	public SkarbonkaDeposit() {
		bank = BankFactory.GetBank(BankCode.VTBBank);
		name = DepositNames.MSG_006_Skarbonka;
		url = "http://vtb-bank.by/personal/deposit/skarbonka/";
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		int depositTerm = 395;
		int term = Math.min(period, depositTerm);
		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(term);
		float _interestRate = interestRate(term);
		float _amount = amount;

		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Depost);

		DateTime previousDate = currentDate;
		currentDate = currentDate.plusMonths(1);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			int _period = Days.daysBetween(previousDate, currentDate).getDays();
			_amount = calculatePeriod(_amount, _interestRate, _period);
			addRecord(list, currentDate, _amount, _interestRate, RecordDescriptions.MSG_001_Capitalization);

			previousDate = currentDate;
			currentDate = currentDate.plusMonths(1);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period != 0) {
			_amount = calculatePeriod(_amount, _interestRate, _period);
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);

		return new DepositAccount(this, list);
	}

	private float interestRate(int _period) {
		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(_period);
		int months = Months.monthsBetween(currentDate, endDate).getMonths();

		return months < 3 ? lowInterestRate : interestRate;
	}
}
