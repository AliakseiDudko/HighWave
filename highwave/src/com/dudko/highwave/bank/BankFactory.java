package com.dudko.highwave.bank;

import java.util.HashMap;
import java.util.Map;

import com.dudko.highwave.bank.banks.Bank;
import com.dudko.highwave.bank.banks.BelAgroPromBank;
import com.dudko.highwave.bank.banks.HomeCreditBank;

public class BankFactory {
	private static final Map<BankCode, Bank> banks;

	static {
		banks = new HashMap<BankCode, Bank>();

		AddBank(new BelAgroPromBank());
		AddBank(new HomeCreditBank());

		banks.values().toArray(new Bank[banks.size()]);
	}

	private static void AddBank(Bank bank) {
		banks.put(bank.code, bank);
	}

	public static Bank GetBank(BankCode bankCode) {
		return banks.get(bankCode);
	}
}
