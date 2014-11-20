package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class IdeaBank extends Bank {
	public IdeaBank() {
		code = BankCode.IdeaBank;
		css = "ib";
		displayName = "Идея Банк";
		url = "http://ideabank.by";
	}
}
