package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.dudko.highwave.bank.BankCode;
import com.dudko.highwave.bank.BankFactory;
import com.dudko.highwave.deposit.AccountStatementRecord;
import com.dudko.highwave.deposit.Currency;
import com.dudko.highwave.deposit.DepositAccount;

public class OnWaveDeposit extends Deposit {
	public OnWaveDeposit() {
		id = 2;
		bank = BankFactory.GetBank(BankCode.HomeCreditBank);
		name = "На волне";
		currency = Currency.BYR;
		interestRate = 25.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		float periodAmount = amount;
		DateTime periodDate = DateTime.now();
		AccountStatementRecord record = new AccountStatementRecord(periodDate, periodAmount, interestRate, "Открытие вклада.");
		list.add(record);

		periodDate = periodDate.plusDays(10);
		periodAmount = calculatePeriod(periodAmount, interestRate, 10);
		record = new AccountStatementRecord(periodDate, periodAmount, interestRate, "Капитализация.");
		list.add(record);

		record = new AccountStatementRecord(periodDate, periodAmount, interestRate, "Закрытие вклада.");
		list.add(record);

		return new DepositAccount(this, list);
	}
}