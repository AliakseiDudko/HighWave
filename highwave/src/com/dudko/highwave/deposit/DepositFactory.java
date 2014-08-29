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
		AddDeposit(new EasyChoiseDeposit(9, "Легкий выбор 30", 29.0f, 30));
		AddDeposit(new EasyChoiseDeposit(10, "Легкий выбор 90", 32.0f, 90));
		AddDeposit(new EasyChoiseDeposit(11, "Легкий выбор 180", 31.0f, 180));
		AddDeposit(new AnnualDeposit());

		depoisitsArray = deposits.values().toArray(new Deposit[deposits.size()]);
	}

	private static void AddDeposit(Deposit deposit) {
		deposits.put(deposit.id, deposit);
	}

	public Deposit[] GetAllDeposits() {
		return depoisitsArray;
	}
}
