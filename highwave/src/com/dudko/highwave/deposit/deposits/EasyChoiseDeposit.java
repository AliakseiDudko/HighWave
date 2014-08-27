package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;

public class EasyChoiseDeposit extends Deposit {
	private int depositTerm;
	private float minOpenAmount = 1000000f;
	private float lowInterestRate = 0.1f;

	public EasyChoiseDeposit(int id, String name, float interestRate, int depositTerm) {
		bank = BankFactory.GetBank(BankCode.HomeCreditBank);
		url = "http://www.homecredit.by/loans_and_services/legkiy_vybor/index.htm";
		currency = Currency.BYR;

		this.id = id;
		this.name = name;
		this.depositTerm = depositTerm;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();

		int term = Math.min(period, depositTerm);
		DateTime currentDate = DateTime.now();
		DateTime endDate = currentDate.plusDays(term);
		DateTime closeDate = currentDate.plusDays(period);
		float _interestRate = interestRate(term);
		float depositAmount = amount;

		AccountStatementRecord record = new AccountStatementRecord(currentDate, depositAmount, _interestRate, "Открытие вклада.");
		list.add(record);

		depositAmount = calculatePeriod(depositAmount, _interestRate, term);
		if (term < depositTerm) {
			record = new AccountStatementRecord(endDate, depositAmount, _interestRate, "Начисление процентов.");
		} else {
			record = new AccountStatementRecord(endDate, depositAmount, _interestRate, "Капитализация.");
		}
		list.add(record);

		if (period > depositTerm) {
			int _period = period - depositTerm;
			depositAmount = calculatePeriod(depositAmount, lowInterestRate, _period);
			record = new AccountStatementRecord(closeDate, depositAmount, lowInterestRate, "Начисление процентов.");
			list.add(record);
		}

		record = new AccountStatementRecord(closeDate, depositAmount, _interestRate, "Закрытие вклада.").setIsLast(true);
		list.add(record);

		return new DepositAccount(this, list);
	}

	private float interestRate(int _period) {
		switch (depositTerm) {
		case 30:
			return interestRate30(_period);
		case 90:
			return interestRate90(_period);
		case 180:
			return interestRate180(_period);
		default:
			return 0.0f;
		}
	}

	private float interestRate30(int _period) {
		if (_period < 30) {
			return 0.1f;
		} else if (_period == 30) {
			return 29.0f;
		}

		return 0.0f;
	}

	private float interestRate90(int _period) {
		if (_period <= 30) {
			return 0.1f;
		} else if (_period <= 60) {
			return 1.0f;
		} else if (_period < 90) {
			return 5.0f;
		} else if (_period == 90) {
			return 32.0f;
		}

		return 0.0f;
	}

	private float interestRate180(int _period) {
		if (_period <= 30) {
			return 0.1f;
		} else if (_period <= 60) {
			return 1.0f;
		} else if (_period <= 90) {
			return 5.0f;
		} else if (_period < 180) {
			return 10.0f;
		} else if (_period == 180) {
			return 31.0f;
		}

		return 0.0f;
	}
}
