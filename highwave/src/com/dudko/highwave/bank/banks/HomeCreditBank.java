package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class HomeCreditBank extends Bank {
	public HomeCreditBank() {
		code = BankCode.HomeCreditBank;
		css = "hcb";
		displayName = "ХоумКредитБанк";
		url = "http://www.homecredit.by/";
	}
}
