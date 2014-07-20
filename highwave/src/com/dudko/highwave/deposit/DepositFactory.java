package com.dudko.highwave.deposit;

import java.util.HashMap;
import java.util.Map;

import com.dudko.highwave.deposit.deposits.Deposit;
import com.dudko.highwave.deposit.deposits.OnWaveDeposit;
import com.dudko.highwave.deposit.deposits.StartDeposit;

public class DepositFactory {
	private static final Map<Integer, Deposit> deposits;
	private static final Deposit[] depoisitsArray;

	static {
		deposits = new HashMap<Integer, Deposit>();

		AddDeposit(new StartDeposit());
		AddDeposit(new OnWaveDeposit());

		depoisitsArray = deposits.values().toArray(new Deposit[deposits.size()]);
	}

	private static void AddDeposit(Deposit deposit) {
		deposits.put(deposit.id, deposit);
	}

	public Deposit GetDeposit(int depositId) {
		return deposits.get(depositId);
	}

	public Deposit[] GetAllDeposits() {
		return depoisitsArray;
	}
}
