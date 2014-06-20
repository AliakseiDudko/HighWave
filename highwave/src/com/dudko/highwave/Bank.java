package com.dudko.highwave;

import com.dudko.highwave.deposit.BankCode;

public class Bank {
	public BankCode code;
	
	public String name;
	
	public String displayName;

	public String url;
	
	public int bankId;

	public Bank(String bankName, int bankId) {
		this.name = bankName;
		this.bankId = bankId;
	}
}