package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.dudko.highwave.bank.BankCode;
import com.dudko.highwave.bank.BankFactory;
import com.dudko.highwave.deposit.AccountStatementRecord;
import com.dudko.highwave.deposit.Currency;
import com.dudko.highwave.deposit.DepositAccount;

public class StartDeposit extends Deposit {
	public StartDeposit() {
		id = 1;
		bank = BankFactory.GetBank(BankCode.BelAgroPromBank);
		name = "Старт!";
		url = "http://www.belapb.by/natural/deposit/start_/";
		currency = Currency.BYR;
		interestRate = 31.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		float periodAmount = amount;
		DateTime periodDate = DateTime.now();
		AccountStatementRecord record = new AccountStatementRecord(periodDate, periodAmount, interestRate, "Открытие вклада.");
		list.add(record);

		for (int i = 0; i < 3; i++) {
			periodDate = periodDate.plusDays(30);
			periodAmount = calculatePeriod(periodAmount, interestRate, 30);
			record = new AccountStatementRecord(periodDate, periodAmount, interestRate, "Капитализация.");
			list.add(record);
		}

		periodDate = periodDate.plusDays(5);
		periodAmount = calculatePeriod(periodAmount, interestRate, 5);
		record = new AccountStatementRecord(periodDate, periodAmount, interestRate, "Закрытие вклада.");
		list.add(record);

		return new DepositAccount(this, list);
	}
}
