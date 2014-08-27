package com.dudko.highwave.deposit;

import java.util.*;

import com.dudko.highwave.deposit.deposits.*;

public class DepositFactory {
	private static final Map<Integer, Deposit> deposits;
	private static final Deposit[] depoisitsArray;

	static {
		deposits = new HashMap<Integer, Deposit>();

		AddDeposit(new StartDeposit());
		AddDeposit(new OnWaveDeposit());
		AddDeposit(new SkarbonkaDeposit());
		AddDeposit(new RapidProfitDeposit());
		AddDeposit(new TwoByTwoDeposit());
		AddDeposit(new MTSquirrels(6, "МТБелки 18-1", 28.0f, 1));
		AddDeposit(new MTSquirrels(7, "МТБелки 18-3", 32.0f, 3));
		AddDeposit(new EarlierMore2Deposit());

		depoisitsArray = deposits.values().toArray(new Deposit[deposits.size()]);
	}

	private static void AddDeposit(Deposit deposit) {
		deposits.put(deposit.id, deposit);
	}

	public Deposit[] GetAllDeposits() {
		return depoisitsArray;
	}
}
