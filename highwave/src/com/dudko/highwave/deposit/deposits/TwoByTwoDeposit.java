package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;

public class TwoByTwoDeposit extends Deposit {
	public TwoByTwoDeposit() {
		id = 5;
		bank = BankFactory.GetBank(BankCode.BelGazPromBank);
		name = "2х2";
		url = "http://belgazprombank.by/personal_banking/vkladi_depoziti/v_nacional_noj_valjute/vklad_2h2/";
		currency = Currency.BYR;
		interestRate = 30.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusMonths(2);

		if (endDate.isAfter(currentDate.plusDays(period))) {
			return null;
		}

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		float depositAmount = amount;
		AccountStatementRecord record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Открытие вклада.");
		list.add(record);

		for (int i = 0; i < 2; i++) {
			currentDate = currentDate.plusMonths(1);
			depositAmount = calculatePeriod(depositAmount, interestRate, 30);
			record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Капитализация.");
			list.add(record);
		}

		record = new AccountStatementRecord(endDate, depositAmount, interestRate, "Закрытие вклада.").setIsLast(true);
		list.add(record);

		return new DepositAccount(this, list);
	}
}
