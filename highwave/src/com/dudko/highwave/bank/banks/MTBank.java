package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class MTBank extends Bank {
	public MTBank(){
		code = BankCode.MTBank;
		css = "mtb";
		displayName = "МТБанк";
		url = "http://www.mtbank.by/";
	}
}