package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.bank.BankCode;
import com.dudko.highwave.deposit.Currency;
import com.dudko.highwave.deposit.Deposit;

public class StartDeposit extends Deposit {
	public StartDeposit() {
		this.id = 1;
		this.displayName = "Старт 45";
		this.currency = Currency.BYR;
		this.bankCode = BankCode.HomeCreditBank;
	}
}
