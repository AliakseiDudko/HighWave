package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class BelGazPromBank extends Bank {
	public BelGazPromBank() {
		code = BankCode.BelGazPromBank;
		css = "bgpb";
		displayName = "Белгазпромбанк";
		url = "http://belgazprombank.by/";
	}
}
