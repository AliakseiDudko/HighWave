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
		
		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		DateTime currentDate = DateTime.now();
		float depositAmount = amount;
		
		AccountStatementRecord record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Открытие вклада.");
		list.add(record);

		for (int i = 0; i < period; i += 10) {
			currentDate = currentDate.plusDays(10);
			depositAmount = calculatePeriod(depositAmount, interestRate, 10);
			
			record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Капитализация.");
			list.add(record);
		}

		record = new AccountStatementRecord(currentDate, depositAmount, interestRate, "Закрытие вклада.").setIsLast(true);
		list.add(record);

		return new DepositAccount(this, list);
	}
}