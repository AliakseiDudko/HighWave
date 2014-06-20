package com.dudko.highwave.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dudko.highwave.bank.banks.BelAgroPromBank;

public class BankFactory {
	private static final Map<BankCode, Bank> banks;

	static {
		banks = new HashMap<BankCode, Bank>();

		banks.put(BankCode.BelAgroPromBank, new BelAgroPromBank());
	}

	public Bank GetBank(BankCode bankCode) {
		return banks.get(bankCode);
	}

	public ArrayList<Bank> GetAllBanks() {
		return new ArrayList<Bank>(banks.values());
	}
}
