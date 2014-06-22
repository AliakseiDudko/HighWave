package com.dudko.highwave.bank;

import java.util.HashMap;
import java.util.Map;

import com.dudko.highwave.bank.banks.BelAgroPromBank;

public class BankFactory {
	private static final Map<BankCode, Bank> banks;
	private static final Bank[] banksArray;

	static {
		banks = new HashMap<BankCode, Bank>();

		banks.put(BankCode.BelAgroPromBank, new BelAgroPromBank());

		banksArray = banks.values().toArray(new Bank[banks.size()]);
	}

	public Bank GetBank(BankCode bankCode) {
		return banks.get(bankCode);
	}

	public Bank[] GetAllBanks() {
		return banksArray;
	}
}
