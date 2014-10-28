package com.dudko.highwave.bank;

import java.util.*;

import com.dudko.highwave.bank.banks.*;

public class BankFactory {
	private static final Map<BankCode, Bank> banks;

	static {
		banks = new HashMap<BankCode, Bank>();

		AddBank(new BelAgroPromBank());
		AddBank(new BelarusBank());
		AddBank(new HomeCreditBank());
		AddBank(new VTBBank());
		AddBank(new BTABank());
		AddBank(new BelGazPromBank());
		AddBank(new MTBank());
		AddBank(new ParitetBank());

		banks.values().toArray(new Bank[banks.size()]);
	}

	private static void AddBank(Bank bank) {
		banks.put(bank.code, bank);
	}

	public static Bank GetBank(BankCode bankCode) {
		return banks.get(bankCode);
	}
}