package com.dudko.highwave.deposit.deposits;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;
import com.dudko.highwave.utils.*;

public class FourSeasonsDeposit extends Deposit {
	public FourSeasonsDeposit() {
		bank = BankFactory.GetBank(BankCode.BelarusBank);
		name = DepositNames.MSG_009_FourSeasons;
		url = "http://belarusbank.by/ru/fizicheskim_licam/vklady/foreign/29540";
		currency = Currency.CUR;
		interestRate = 3.5f;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		float minOpenAmount = 500.0f;
		float minDepositAmount = 500.0f;
		if (amount < minOpenAmount) {
			return null;
		}

		LocalDate currentDate = MinskLocalDate.now();
		if (period < Days.daysBetween(currentDate, currentDate.plusMonths(1)).getDays()) {
			return null;
		}

		LocalDate endDate = currentDate.plusMonths(12);
		int fullTerm = Days.daysBetween(currentDate, endDate).getDays();
		int partialTerm = Math.min(fullTerm, period);
		LocalDate partialEndDate = currentDate.plusDays(partialTerm);

		float depositAmount = amount;

		DepositAccount account = new DepositAccount(this);
		account.addRecord(currentDate, depositAmount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		LocalDate previousDate = currentDate;
		LocalDate seasonStartDate = currentDate;
		currentDate = previousDate.plusMonths(1);
		for (int month = 1; month <= 12; month++) {
			depositAmount = calculatePeriod(depositAmount, interestRate, previousDate, currentDate);
			account.addRecord(currentDate, depositAmount, interestRate, RecordDescriptions.MSG_001_Capitalization);

			if (month % 3 == 0) {
				float[] bonusInterests = { 0.5f, 1.0f, 1.5f, 2.0f };
				float _bonusInterest = bonusInterests[month / 3 - 1];

				depositAmount = calculatePeriod(depositAmount, _bonusInterest, seasonStartDate, currentDate);
				account.addRecord(currentDate, depositAmount, _bonusInterest, RecordDescriptions.MSG_007_Bonus);

				seasonStartDate = currentDate;
			}

			previousDate = currentDate;
			currentDate = previousDate.plusMonths(1);

			boolean withdrawal = (previousDate.isBefore(partialEndDate) || previousDate.isEqual(partialEndDate)) && currentDate.isAfter(partialEndDate) && month != 12;
			if (withdrawal) {
				account.addRecord(previousDate, depositAmount, interestRate, RecordDescriptions.MSG_006_Partial_Withdrawal_Of_Deposit, true);
				depositAmount = minDepositAmount;
			}
		}

		account.addRecord(endDate, depositAmount, interestRate, RecordDescriptions.MSG_003_Close_Deposit, partialEndDate.isEqual(endDate));

		account.fillData();
		return account;
	}
}