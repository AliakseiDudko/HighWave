package com.dudko.highwave.deposit;

import java.util.*;

import com.dudko.highwave.deposit.deposits.*;

public class DepositFactory {
	private static final List<Deposit> deposits;

	static {
		deposits = new ArrayList<Deposit>();

		deposits.add(new StartDeposit());
		deposits.add(new OnWaveDeposit());
		deposits.add(new SkarbonkaDeposit());
		deposits.add(new RapidProfitDeposit());
		deposits.add(new TwoByTwoDeposit());
		deposits.add(new MTSquirrels());
		deposits.add(new EarlierMore2Deposit());
		deposits.add(new EasyChoiseDeposit("Легкий выбор 30", 29.0f, 30));
		deposits.add(new EasyChoiseDeposit("Легкий выбор 90", 30.0f, 90));
		deposits.add(new EasyChoiseDeposit("Легкий выбор 180", 31.0f, 180));
		deposits.add(new AnnualDeposit());
	}

	public Deposit[] GetAllDeposits(Currency currency) {
		List<Deposit> list = new ArrayList<Deposit>();

		for (Deposit deposit : deposits) {
			if (deposit.currency == currency) {
				list.add(deposit);
			}
		}

		return list.toArray(new Deposit[list.size()]);
	}
}
