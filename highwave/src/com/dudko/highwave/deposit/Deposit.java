package com.dudko.highwave.deposit;

import com.dudko.highwave.bank.BankCode;

public abstract class Deposit {
	public int id;

	public String displayName;

	public Currency currency;

	public BankCode bankCode;
}
