package com.dudko.highwave.deposit.deposits;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class TwoByTwoDeposit extends Deposit {
	public TwoByTwoDeposit() {
		bank = BankFactory.GetBank(BankCode.BelGazPromBank);
		name = DepositNames.MSG_008_TwoByTwo;
		url = "http://belgazprombank.by/personal_banking/vkladi_depoziti/v_nacional_noj_valjute/vklad_2h2/";
		currency = Currency.BYR;
		interestRate = 26.0f;
	}

	@Override
	public DepositAccount calculateDeposit(long amount, int period) {
		LocalDate currentDate = MinskLocalDate.now();
		LocalDate endDate = currentDate.plusMonths(2);

		if (endDate.isAfter(currentDate.plusDays(period))) {
			return null;
		}

		float _amount = amount;

		DepositAccount account = new DepositAccount(this);
		account.addRecord(currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		for (int i = 0; i < 2; i++) {
			LocalDate previousDate = currentDate;
			currentDate = currentDate.plusMonths(1);

			_amount = calculatePeriod(_amount, interestRate, previousDate, currentDate);
			account.addRecord(currentDate, _amount, interestRate, RecordDescriptions.MSG_001_Capitalization);
		}

		account.addRecord(endDate, _amount, interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);

		account.fillData();
		return account;
	}
}