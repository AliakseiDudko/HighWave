package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;

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
		interestRate = 26.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		period = Math.min(period, 30);

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(period);
		float depositAmount = amount;

		AccountStatementRecord record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Открытие вклада.");
		list.add(record);

		DateTime previousDate = currentDate;
		currentDate = currentDate.plusDays(depositTerm);

		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			depositAmount = calculatePeriod(depositAmount, interestRate, depositTerm);
			record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Капитализация.");
			list.add(record);

			previousDate = currentDate;
			currentDate = currentDate.plusDays(depositTerm);
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
