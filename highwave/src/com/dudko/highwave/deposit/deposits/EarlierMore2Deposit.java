package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;

public class EarlierMore2Deposit extends Deposit {
	private int depositTerm = 270;
	private int capitalizationPeriod = 30;
	private float minOpenAmount = 1000000f;
	private float lowInterestRate = 0.1f;

	public EarlierMore2Deposit() {
		id = 8;
		bank = BankFactory.GetBank(BankCode.HomeCreditBank);
		name = "Раньше-Больше 2";
		url = "http://homecredit.by/loans_and_services/Ranshe_Bolshe_2/";
		currency = Currency.BYR;
		interestRate = 30.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		int term = Math.min(period, depositTerm);
		DateTime currentDate = DateTime.now();
		DateTime closeDate = currentDate.plusDays(term);
		float _interestRate = interestRate(term);
		float depositAmount = amount;

		AccountStatementRecord record = new AccountStatementRecord(currentDate, depositAmount, _interestRate, "Открытие вклада.");
		list.add(record);

		DateTime previousDate = currentDate;
		currentDate = currentDate.plusDays(capitalizationPeriod);
		while (currentDate.isBefore(closeDate) || currentDate.isEqual(closeDate)) {
			depositAmount = calculatePeriod(depositAmount, _interestRate, capitalizationPeriod);
			record = new AccountStatementRecord(currentDate, depositAmount, _interestRate, "Капитализация.");
			list.add(record);

			previousDate = currentDate;
			currentDate = currentDate.plusDays(capitalizationPeriod);
		}

		if (period <= depositTerm) {
			int _period = Days.daysBetween(previousDate, closeDate).getDays();
			depositAmount = calculatePeriod(depositAmount, _interestRate, _period);
			record = new AccountStatementRecord(closeDate, depositAmount, _interestRate, "Закрытие вклада.").setIsLast(true);
			list.add(record);
		} else {
			int _period = period - depositTerm;
			currentDate = previousDate.plusDays(_period);
			depositAmount = calculatePeriod(depositAmount, lowInterestRate, _period);
			record = new AccountStatementRecord(currentDate, depositAmount, lowInterestRate, "Закрытие вклада.").setIsLast(true);
			list.add(record);
		}

		return new DepositAccount(this, list);
	}

	private float interestRate(int _period) {
		if (_period <= 90) {
			return 0.1f;
		} else if (_period <= 105) {
			return 32.0f;
		} else if (_period <= 120) {
			return 30.0f;
		} else if (_period <= 135) {
			return 28.0f;
		} else if (_period <= 150) {
			return 26.0f;
		} else if (_period <= 165) {
			return 24.0f;
		} else if (_period <= 180) {
			return 22.0f;
		} else if (_period <= depositTerm) {
			return 20.0f;
		}

		return 0f;
	}
}
