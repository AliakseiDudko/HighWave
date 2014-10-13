package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class BelAgroPromBank extends Bank {
	public BelAgroPromBank() {
		code = BankCode.BelAgroPromBank;
		css = "bapb";
		displayName = "БелАгроПромБанк";
		url = "http://www.belapb.by";
	}
}
