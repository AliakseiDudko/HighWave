package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

		List<Integer> days = Arrays.asList(capitalizationPeriod, 2 * capitalizationPeriod, 3 * capitalizationPeriod);

		for (int day = 1; day <= depositTerm; day++) {
			int _period = 0;
			currentDate = currentDate.plusDays(1);

			if (days.contains(day)) {
				if (day == days.get(1) && period > days.get(0) && period < days.get(1)) {
					_period = days.get(1) - period;
				} else if (day == days.get(2) && period > days.get(1) && period < days.get(2)) {
					_period = days.get(2) - period;
				} else {
					_period = capitalizationPeriod;
				}

				depositAmount = calculatePeriod(depositAmount, interestRate, _period);
				record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Капитализация.");
				list.add(record);
			}

			if (day == depositTerm) {
				if (period > days.get(2) && period < depositTerm) {
					_period = depositTerm - period;
				} else {
					_period = day - days.get(2);
				}

				depositAmount = calculatePeriod(depositAmount, interestRate, _period);
				record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Закрытие вклада.").setIsLast(true);
				list.add(record);
			} else if (day == period) {
				_period = day % capitalizationPeriod;

				depositAmount = calculatePeriod(depositAmount, interestRate, _period);
				record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Частичное снятие вклада.").setIsLast(true);
				list.add(record);

				depositAmount = minDepositAmount;
			}
		}

		return new DepositAccount(this, list);
	}
}
