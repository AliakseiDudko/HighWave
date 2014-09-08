package com.dudko.highwave.deposit.deposits;

import java.util.*;

import org.joda.time.DateTime;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.deposit.Currency;

public class StartDeposit extends Deposit {
	private int depositTerm = 95;
	private int capitalizationPeriod = 30;
	private float minOpenAmount = 1000000f;
	private float minDepositAmount = 3000000f;

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
		float _amount = amount;

		addRecord(list, currentDate, _amount, interestRate, "Открытие вклада.");

		Set<Integer> setOfDays = new TreeSet<Integer>(Arrays.asList(0, 30, 60, 90, 95, period));
		Integer[] days = setOfDays.toArray(new Integer[0]);

		for (int i = 0; days[i] < depositTerm; i++) {
			int day = days[i + 1];
			int _period = day - days[i];

			currentDate = currentDate.plusDays(_period);
			_amount = calculatePeriod(_amount, interestRate, _period);

			boolean isLast = day == period || (day == depositTerm && depositTerm < period);

			if (day % capitalizationPeriod == 0) {
				addRecord(list, currentDate, _amount, interestRate, "Капитализация.");
			}

			if (day == depositTerm) {
				addRecord(list, currentDate, _amount, interestRate, "Закрытие вклада.", isLast);
			} else if (day == period) {
				addRecord(list, currentDate, _amount, interestRate, "Частичное снятие вклада.", isLast);
				_amount = minDepositAmount;
			}
		}

		return new DepositAccount(this, list);
	}
}
