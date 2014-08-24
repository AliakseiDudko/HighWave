package com.dudko.highwave.deposit.deposits;

import java.util.*;

import org.joda.time.DateTime;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.deposit.Currency;

public class OnWaveDeposit extends Deposit {
	private int depositTerm = 10;
	private float minOpenAmount = 1000000;

	public OnWaveDeposit() {
		id = 2;
		bank = BankFactory.GetBank(BankCode.HomeCreditBank);
		name = "На волне";
		url = "http://www.homecredit.by/loans_and_services/na_volne/";
		currency = Currency.BYR;
		interestRate = 22.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount || period < depositTerm) {
			return null;
		}

		period = Math.min(period, 30);

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(period);
		float depositAmount = amount;

		AccountStatementRecord record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Открытие вклада.");
		list.add(record);

		currentDate = currentDate.plusDays(depositTerm);

		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			depositAmount = calculatePeriod(depositAmount, interestRate, depositTerm);
			record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Капитализация.");
			list.add(record);

			currentDate = currentDate.plusDays(depositTerm);
		}

		record = new AccountStatementRecord(endDate, depositAmount, interestRate, "Закрытие вклада.").setIsLast(true);
		list.add(record);

		return new DepositAccount(this, list);
	}
}