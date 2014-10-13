package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class BTABank extends Bank {
	public BTABank() {
		code = BankCode.BTABank;
		css = "bta";
		displayName = "БТА Банк";
		url = "http://www.btabank.by";
	}
}
