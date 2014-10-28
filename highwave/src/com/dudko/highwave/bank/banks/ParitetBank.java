package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class ParitetBank extends Bank {
	public ParitetBank() {
		code = BankCode.ParitetBank;
		css = "prt";
		displayName = "Паритетбанк";
		url = "http://www.paritetbank.by";
	}
}