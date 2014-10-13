package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class BelarusBank extends Bank {
	public BelarusBank() {
		code = BankCode.BelarusBank;
		css = "bb";
		displayName = "Беларусбанк";
		url = "http://belarusbank.by/";
	}
}