package com.dudko.highwave.deposit.deposits;

import com.dudko.highwave.deposit.BankCode;
import com.dudko.highwave.deposit.Currency;

public abstract class Deposit {
	public int id;

	public String displayName;

	public Currency currency;

	public BankCode bankCode;
}
