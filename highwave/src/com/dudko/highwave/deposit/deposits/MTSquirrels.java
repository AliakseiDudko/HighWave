package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;

public class MTSquirrels extends Deposit {
	private float minOpenAmount = 500000f;
	private float lowInterestRate = 1.0f;
	private int fixPeriodMonths;

	public MTSquirrels(int id, String name, float interestRate, int fixPeriodMonths) {
		bank = BankFactory.GetBank(BankCode.MTBank);
		url = "http://www.mtbank.by/private/deposits/br/mtbelki/";
		currency = Currency.BYR;

		this.id = id;
		this.name = name;
		this.interestRate = interestRate;
		this.fixPeriodMonths = fixPeriodMonths;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		DateTime currentDate = DateTime.now();
		DateTime maxEndDate = DateTime.now().plusMonths(18);

		period = Math.min(Days.daysBetween(currentDate, maxEndDate).getDays(), period);
		DateTime endDate = currentDate.plusDays(period);

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		float depositAmount = amount;

		AccountStatementRecord record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Открытие вклада.");
		list.add(record);

		DateTime previousDate = currentDate;
		DateTime fixPeriodDate = currentDate.plusMonths(fixPeriodMonths);
		currentDate = currentDate.plusMonths(1);

		while (fixPeriodDate.isBefore(endDate) || fixPeriodDate.isEqual(endDate)) {
			for (int i = 0; i < fixPeriodMonths; i++) {
				int _period = Days.daysBetween(previousDate, currentDate).getDays();
				depositAmount = calculatePeriod(depositAmount, interestRate, _period);
				record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Капитализация.");
				list.add(record);

				previousDate = currentDate;
				currentDate = currentDate.plusMonths(1);
			}

			fixPeriodDate = fixPeriodDate.plusMonths(fixPeriodMonths);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		depositAmount = calculatePeriod(depositAmount, lowInterestRate, _period);
		if (_period == 0) {
			record = new AccountStatementRecord(endDate, depositAmount, interestRate, "Закрытие вклада.").setIsLast(true);
			list.add(record);
		} else {
			record = new AccountStatementRecord(endDate, depositAmount, lowInterestRate, "Досрочное истребование депозита.");
			list.add(record);
			record = new AccountStatementRecord(endDate, depositAmount, lowInterestRate, "Закрытие вклада.").setIsLast(true);
			list.add(record);
		}

		return new DepositAccount(this, list);
	}
}
