package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.RecordDescriptions;

public class EarlierMore2Deposit extends Deposit {
	private int depositTerm = 270;
	private int capitalizationPeriod = 30;
	private float minOpenAmount = 1000000f;
	private float lowInterestRate = 0.1f;

	public EarlierMore2Deposit() {
		bank = BankFactory.GetBank(BankCode.HomeCreditBank);
		name = "Раньше-Больше 2";
		url = "http://homecredit.by/loans_and_services/Ranshe_Bolshe_2/";
		currency = Currency.BYR;
		interestRate = 28.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		int term = Math.min(period, depositTerm);
		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(term);
		float _interestRate = interestRate(term);
		float depositAmount = amount;

		addRecord(list, currentDate, depositAmount, interestRate, RecordDescriptions.MSG_000_Open_Depost);

		DateTime previousDate = currentDate;
		currentDate = currentDate.plusDays(capitalizationPeriod);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			depositAmount = calculatePeriod(depositAmount, _interestRate, capitalizationPeriod);
			addRecord(list, currentDate, depositAmount, _interestRate, RecordDescriptions.MSG_001_Capitalization);

			previousDate = currentDate;
			currentDate = currentDate.plusDays(capitalizationPeriod);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period > 0) {
			depositAmount = calculatePeriod(depositAmount, _interestRate, _period);
			addRecord(list, endDate, depositAmount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		if (period <= depositTerm) {
			addRecord(list, endDate, depositAmount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			_period = period - depositTerm;
			currentDate = endDate.plusDays(_period);
			depositAmount = calculatePeriod(depositAmount, lowInterestRate, _period);
			addRecord(list, currentDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
			addRecord(list, currentDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		}

		return new DepositAccount(this, list);
	}

	private float interestRate(int _period) {
		if (_period <= 90) {
			return 0.1f;
		} else if (_period <= 105) {
			return 30.0f;
		} else if (_period <= 120) {
			return 28.0f;
		} else if (_period <= 135) {
			return 26.0f;
		} else if (_period <= 150) {
			return 24.0f;
		} else if (_period <= 165) {
			return 22.0f;
		} else if (_period <= 180) {
			return 20.0f;
		} else if (_period <= depositTerm) {
			return 18.0f;
		}

		return lowInterestRate;
	}
}
