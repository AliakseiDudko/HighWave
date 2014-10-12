package com.dudko.highwave.deposit.deposits;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public class EasyChoiseDeposit extends Deposit {
	private int depositTerm;
	private float minOpenAmount = 1000000f;
	private float lowInterestRate = 0.1f;

	public EasyChoiseDeposit(DepositNames name, float interestRate, int depositTerm) {
		bank = BankFactory.GetBank(BankCode.HomeCreditBank);
		url = "http://www.homecredit.by/loans_and_services/legkiy_vybor/index.htm";
		currency = Currency.BYR;

		this.name = name;
		this.interestRate = interestRate;
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
		float _interestRate = interestRate(term);
		float depositAmount = amount;

		addRecord(list, currentDate, depositAmount, interestRate, RecordDescriptions.MSG_000_Open_Depost);

		currentDate = currentDate.plusDays(term);
		depositAmount = calculatePeriod(depositAmount, _interestRate, term);
		if (term == depositTerm) {
			addRecord(list, currentDate, depositAmount, interestRate, RecordDescriptions.MSG_001_Capitalization);
		} else {
			addRecord(list, currentDate, depositAmount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
		}

		if (period <= depositTerm) {
			addRecord(list, endDate, depositAmount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else {
			int _period = period - depositTerm;
			currentDate = endDate.plusDays(_period);
			depositAmount = calculatePeriod(depositAmount, lowInterestRate, _period);
			addRecord(list, currentDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
			addRecord(list, currentDate, depositAmount, lowInterestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		}

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
			return interestRate;
		}

		return lowInterestRate;
	}

	private float interestRate90(int _period) {
		if (_period <= 30) {
			return 0.1f;
		} else if (_period <= 60) {
			return 1.0f;
		} else if (_period < 90) {
			return 5.0f;
		} else if (_period == 90) {
			return interestRate;
		}

		return lowInterestRate;
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
			return interestRate;
		}

		return lowInterestRate;
	}
}
