package com.dudko.highwave.bank.banks;

import com.dudko.highwave.bank.BankCode;

public class MoskowMinskBank extends Bank {
	public MoskowMinskBank() {
		code = BankCode.MoskowMinskBank;
		css = "mmb";
		displayName = "Банк Москва-Минск";
		url = "http://www.mmbank.by";
	}
}
