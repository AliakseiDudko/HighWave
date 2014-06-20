package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.BankCode;
import com.dudko.highwave.deposit.Currency;

public class StartDeposit extends Deposit {
	public StartDeposit() {
		this.id = 1;
		this.displayName = "Старт 45";
		this.currency = Currency.BYR;
		this.bankCode = BankCode.HomeCreditBank;
	}
}
