package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;

public class MTSquirrels extends Deposit {
	private float minOpenAmount = 500000f;
	private float lowInterestRate = 1.0f;
	private int fixPeriodMonths = 2;

	public MTSquirrels() {
		id = 6;
		bank = BankFactory.GetBank(BankCode.MTBank);
		name = "МТБелки 18-2";
		url = "http://www.mtbank.by/private/deposits/br/mtbelki/";
		currency = Currency.BYR;
		interestRate = 30.0f;
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

		addRecord(list, currentDate, depositAmount, interestRate, "Открытие вклада.");

		DateTime previousDate = currentDate;
		DateTime fixPeriodDate = currentDate.plusMonths(fixPeriodMonths);
		currentDate = currentDate.plusMonths(1);

		int months = 0;
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			float _interestRate = fixPeriodDate.isBefore(endDate) || fixPeriodDate.isEqual(endDate) ? interestRate : lowInterestRate;
			for (int i = 0; i < fixPeriodMonths && currentDate.isBefore(endDate) || currentDate.isEqual(endDate); i++) {
				int _period = Days.daysBetween(previousDate, currentDate).getDays();
				depositAmount = calculatePeriod(depositAmount, _interestRate, _period);
				addRecord(list, currentDate, depositAmount, _interestRate, "Капитализация.");

				months++;
				previousDate = currentDate;
				currentDate = currentDate.plusMonths(1);
			}

			int _period = Days.daysBetween(previousDate, endDate).getDays();
			if (_period > 0 && months != 0 && months % fixPeriodMonths == 0) {
				depositAmount *= 1.005f;
				addRecord(list, currentDate, depositAmount, interestRate, "Бонус 0,5%.");
			}

			fixPeriodDate = fixPeriodDate.plusMonths(fixPeriodMonths);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		if (_period != 0) {
			depositAmount = calculatePeriod(depositAmount, lowInterestRate, _period);
			addRecord(list, endDate, depositAmount, lowInterestRate, "Начисление процентов.");
		}

		if (months % fixPeriodMonths == 0 && _period == 0) {
			addRecord(list, endDate, depositAmount, interestRate, "Закрытие вклада.", true);
		} else {
			addRecord(list, endDate, depositAmount, lowInterestRate, "Досрочное закрытие вклада.", true);
		}

		return new DepositAccount(this, list);
	}
}
