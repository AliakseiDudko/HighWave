package com.dudko.highwave.deposit.deposits;

import java.util.*;

import org.joda.time.*;

import com.dudko.highwave.bank.*;
import com.dudko.highwave.deposit.*;
import com.dudko.highwave.globalize.*;

public abstract class MagicianPlusDeposit extends Deposit {
	protected float minOpenAmount;
	protected float minDepositAmount;
	protected float bonusInterest;
	private int depositTermMonths;

	public MagicianPlusDeposit(int depositTermMonths) {
		bank = BankFactory.GetBank(BankCode.MoskowMinskBank);
		name = depositTermMonths == 9 ? DepositNames.MSG_017_MagicianPlus9 : DepositNames.MSG_017_MagicianPlus12;
		url = "http://www.mmbank.by/personal/deposites/vklad_charodej/";

		this.depositTermMonths = depositTermMonths;
	}

	@Override
	public DepositAccount calculateDeposit(float amount, int period) {
		if (amount < minOpenAmount) {
			return null;
		}

		DateTime currentDate = DateTime.now();
		DateTime treeMonthsDate = currentDate.plusMonths(3);
		int threeMonthsTerm = Days.daysBetween(currentDate, treeMonthsDate).getDays();
		DateTime maxEndDate = currentDate.plusMonths(depositTermMonths);
		int depositTerm = Days.daysBetween(currentDate, maxEndDate).getDays();
		int term = Math.min(period, depositTerm);
		DateTime endDate = currentDate.plusDays(term);

		float _amount = amount;
		float _interestRate = term < threeMonthsTerm ? 1.0f : interestRate;

		List<AccountStatementRecord> list = new ArrayList<AccountStatementRecord>();
		addRecord(list, currentDate, _amount, interestRate, RecordDescriptions.MSG_000_Open_Deposit);

		Set<Integer> setOfCapitalization = new TreeSet<Integer>();
		DateTime futureDate = currentDate.plusMonths(1);
		while (futureDate.isBefore(maxEndDate) || futureDate.isEqual(maxEndDate)) {
			int _period = Days.daysBetween(currentDate, futureDate).getDays();
			setOfCapitalization.add(_period);

			futureDate = futureDate.plusMonths(1);
		}

		Set<Integer> setOfDays = new TreeSet<Integer>();
		setOfDays.addAll(Arrays.asList(0, term, depositTerm));
		setOfDays.addAll(setOfCapitalization);
		Integer[] days = setOfDays.toArray(new Integer[0]);

		for (int i = 0; days[i] < depositTerm; i++) {
			int day = days[i + 1];
			int _period = day - days[i];

			_amount = calculatePeriod(_amount, _interestRate, _period);

			currentDate = currentDate.plusDays(_period);
			if (setOfCapitalization.contains(day)) {
				addRecord(list, currentDate, _amount, _interestRate, RecordDescriptions.MSG_001_Capitalization);
			} else {
				addRecord(list, currentDate, _amount, _interestRate, RecordDescriptions.MSG_002_Accrual_Of_Interest);
			}

			if (day == term && term < threeMonthsTerm) {
				break;
			} else if (day == term && term < depositTerm) {
				addRecord(list, currentDate, _amount, _interestRate, RecordDescriptions.MSG_006_Partial_Withdrawal_Of_Deposit, true);
				_amount = minDepositAmount;
			}
		}

		if (term == depositTerm) {
			_amount = calculatePeriod(_amount, bonusInterest, depositTerm);
			addRecord(list, endDate, _amount, bonusInterest, RecordDescriptions.MSG_007_Bonus);
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit, true);
		} else if (term < threeMonthsTerm) {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_005_Early_Withdrawal_Of_Deposit, true);
		} else {
			addRecord(list, endDate, _amount, _interestRate, RecordDescriptions.MSG_003_Close_Deposit);
		}

		return new DepositAccount(this, list);
	}
}
