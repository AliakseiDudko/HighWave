package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;

public class AnnualDeposit extends Deposit {
	private int depositTerm = 365;
	private float minOpenAmount = 1000000f;
	private float minInterestRate = 0.1f;

	public AnnualDeposit() {
		id = 12;
		bank = BankFactory.GetBank(BankCode.BelGazPromBank);
		name = "Годовой";
		url = "http://belgazprombank.by/personal_banking/vkladi_depoziti/v_nacional_noj_valjute/vklad_godovoj/";
		currency = Currency.BYR;
		interestRate = 31.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount || period <= 30) {
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
		currentDate = currentDate.plusMonths(1);
		while (currentDate.isBefore(closeDate) || currentDate.isEqual(closeDate)) {
			int _period = Days.daysBetween(previousDate, currentDate).getDays();
			depositAmount = calculatePeriod(depositAmount, _interestRate, _period);
			record = new AccountStatementRecord(currentDate, depositAmount, _interestRate, "Капитализация.");
			list.add(record);

			previousDate = currentDate;
			currentDate = currentDate.plusMonths(1);
		}

		if (period <= depositTerm) {
			int _period = Days.daysBetween(previousDate, closeDate).getDays();
			depositAmount = calculatePeriod(depositAmount, _interestRate, _period);
			record = new AccountStatementRecord(closeDate, depositAmount, _interestRate, "Закрытие вклада.").setIsLast(true);
			list.add(record);
		} else {
			int _period = period - depositTerm;
			currentDate = previousDate.plusDays(_period);
			depositAmount = calculatePeriod(depositAmount, minInterestRate, _period);
			record = new AccountStatementRecord(currentDate, depositAmount, minInterestRate, "Закрытие вклада.").setIsLast(true);
			list.add(record);
		}

		return new DepositAccount(this, list);
	}

	private float interestRate(int _period) {
		if (_period <= 90) {
			return interestRate - 2.0f;
		} else {
			return interestRate;
		}
	}
}
