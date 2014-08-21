package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;

import com.dudko.highwave.bank.BankCode;
import com.dudko.highwave.bank.BankFactory;
import com.dudko.highwave.deposit.AccountStatementRecord;
import com.dudko.highwave.deposit.Currency;
import com.dudko.highwave.deposit.DepositAccount;

public class StartDeposit extends Deposit {
	private int depositTerm = 95;
	private int capitalizationPeriod = 30;
	private float minOpenAmount = 1000000;
	private float minDepositAmount = 3000000;

	public StartDeposit() {
		id = 1;
		bank = BankFactory.GetBank(BankCode.BelAgroPromBank);
		name = "Старт!";
		url = "http://www.belapb.by/natural/deposit/start_/";
		currency = Currency.BYR;
		interestRate = 29.0f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount || period < capitalizationPeriod) {
			return null;
		} else if (amount < minDepositAmount && period < depositTerm) {
			return null;
		}

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		DateTime currentDate = DateTime.now();
		float depositAmount = amount;

		AccountStatementRecord record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Открытие вклада.");
		list.add(record);

		Set<Integer> setOfDays = new TreeSet<Integer>(Arrays.asList(0, 30, 60, 90, 95, period));
		Integer[] days = setOfDays.toArray(new Integer[0]);

		for (int i = 0; days[i] < depositTerm; i++) {
			int day = days[i + 1];
			int _period = day - days[i];

			currentDate = currentDate.plusDays(_period);
			depositAmount = calculatePeriod(depositAmount, interestRate, _period);

			boolean isLast = day == period || (day == depositTerm && depositTerm < period);

			if (day % capitalizationPeriod == 0) {
				record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Капитализация.");
				list.add(record);
			}

			if (day == depositTerm) {
				record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Закрытие вклада.").setIsLast(isLast);
				list.add(record);
			} else if (day == period) {
				record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Частичное снятие вклада.").setIsLast(true);
				list.add(record);

				depositAmount = minDepositAmount;
			}
		}

		return new DepositAccount(this, list);
	}
}
