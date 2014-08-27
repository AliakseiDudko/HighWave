package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;

public class SkarbonkaDeposit extends Deposit {
	private int depositTerm = 395;
	private float minOpenAmount = 200000f;

	public SkarbonkaDeposit() {
		id = 3;
		bank = BankFactory.GetBank(BankCode.VTBBank);
		name = "Скарбонка";
		url = "http://vtb-bank.by/personal/deposit/skarbonka/";
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
		DateTime endDate = currentDate.plusDays(Math.min(period, depositTerm));
		float depositAmount = amount;

		AccountStatementRecord record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Открытие вклада.");
		list.add(record);

		DateTime previousDate = currentDate;
		currentDate = currentDate.plusMonths(1);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			int _period = Days.daysBetween(previousDate, currentDate).getDays();
			depositAmount = calculatePeriod(depositAmount, interestRate, _period);
			record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Капитализация.");
			list.add(record);

			previousDate = currentDate;
			currentDate = currentDate.plusMonths(1);
		}

		int _period = Days.daysBetween(previousDate, endDate).getDays();
		depositAmount = calculatePeriod(depositAmount, interestRate, _period);
		record = new AccountStatementRecord(endDate, depositAmount, interestRate, "Закрытие вклада.").setIsLast(true);
		list.add(record);

		return new DepositAccount(this, list);
	}
}
