package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class TechnoBank extends Bank {
	public TechnoBank() {
		code = BankCode.TechnoBank;
		css = "tb";
		displayName = "Технобанк";
		url = "http://tb.by";
	}
}