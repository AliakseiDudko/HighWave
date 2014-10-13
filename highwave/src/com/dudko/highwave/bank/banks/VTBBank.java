package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class VTBBank extends Bank {
	public VTBBank() {
		code = BankCode.VTBBank;
		css = "vtb";
		displayName = "Банк ВТБ";
		url = "http://vtb-bank.by";
	}
}
