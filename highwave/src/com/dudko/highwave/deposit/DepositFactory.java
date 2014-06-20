package com.dudko.highwave.deposit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dudko.highwave.deposit.deposits.Deposit;
import com.dudko.highwave.deposit.deposits.StartDeposit;

public class DepositFactory {
	private static Map<Integer, Deposit> deposites;

	static {
		deposites = new HashMap<Integer, Deposit>();

		deposites.put(1, new StartDeposit());
		deposites.put(2, new StartDeposit());
		deposites.put(3, new StartDeposit());
		deposites.put(4, new StartDeposit());
		deposites.put(5, new StartDeposit());
		deposites.put(6, new StartDeposit());
		deposites.put(7, new StartDeposit());
	}

	public Deposit GetDeposit(int depositId) {
		return deposites.get(depositId);
	}

	public ArrayList<Deposit> GetAllDeposits() {
		return new ArrayList<Deposit>(deposites.values());
	}
}
