package com.dudko.highwave.deposit;

import java.util.HashMap;
import java.util.Map;

import com.dudko.highwave.deposit.deposits.StartDeposit;

public class DepositFactory {
	private static final Map<Integer, Deposit> deposits;
	private static final Deposit[] depoisitsArray;

	static {
		deposits = new HashMap<Integer, Deposit>();

		deposits.put(1, new StartDeposit());
		deposits.put(2, new StartDeposit());
		deposits.put(3, new StartDeposit());
		deposits.put(4, new StartDeposit());
		deposits.put(5, new StartDeposit());
		deposits.put(6, new StartDeposit());
		deposits.put(7, new StartDeposit());

		depoisitsArray = deposits.values().toArray(new Deposit[deposits.size()]);
	}

	public Deposit GetDeposit(int depositId) {
		return deposits.get(depositId);
	}

	public Deposit[] GetAllDeposits() {
		return depoisitsArray;
	}
}
