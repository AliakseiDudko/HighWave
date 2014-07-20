package com.dudko.highwave.deposit;

import java.util.List;

import com.dudko.highwave.deposit.deposits.Deposit;

public class DepositAccount {
	public Deposit deposit;
	public float startAmount;
	public float endAmount;
	public float profit;
	public float profitRate;
	public AccountStatementRecord[] accountStatement;

	public DepositAccount(Deposit deposit, List<AccountStatementRecord> accountStatement) {
		this.deposit = deposit;

		if (!accountStatement.isEmpty()) {
			this.startAmount = accountStatement.get(0).amount;
			this.endAmount = accountStatement.get(accountStatement.size() - 1).amount;
			this.profit = this.endAmount - this.startAmount;
			this.profitRate = 100.0f * (this.endAmount / this.startAmount - 1.0f);
		}

		this.accountStatement = accountStatement.toArray(new AccountStatementRecord[accountStatement.size()]);
	}
}
