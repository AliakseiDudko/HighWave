package com.dudko.highwave.deposit;

import java.util.*;

import com.dudko.highwave.deposit.deposits.*;
import com.dudko.highwave.globalize.DepositNames;

public class DepositFactory {
	private static final List<Deposit> deposits;

	static {
		deposits = new ArrayList<Deposit>();

		deposits.add(new StartDepositBYR());
		deposits.add(new StartDepositCUR());
		deposits.add(new OnWaveDeposit());
		deposits.add(new SkarbonkaDepositBYR());
		deposits.add(new SkarbonkaDepositCUR());
		deposits.add(new RapidProfitDeposit());
		deposits.add(new TwoByTwoDeposit());
		deposits.add(new MTSquirrelsDeposit(DepositNames.MSG_003_MTSquirrels1, 1));
		deposits.add(new MTSquirrelsDeposit(DepositNames.MSG_003_MTSquirrels2, 2));
		deposits.add(new MTSquirrelsDeposit(DepositNames.MSG_003_MTSquirrels3, 3));
		deposits.add(new EarlierMore2Deposit());
		deposits.add(new EasyChoiseDeposit(DepositNames.MSG_002_EasyChoise30, 30));
		deposits.add(new EasyChoiseDeposit(DepositNames.MSG_002_EasyChoise90, 90));
		deposits.add(new EasyChoiseDeposit(DepositNames.MSG_002_EasyChoise180, 180));
		deposits.add(new AnnualDeposit());
		deposits.add(new FourSeasonsDeposit());
		deposits.add(new CatchMomentDepositEUR());
		deposits.add(new CatchMomentDepositUSD());
		deposits.add(new SavingDeposit());
		deposits.add(new SuccessfulDeposit());
		deposits.add(new PrudentDeposit());
		deposits.add(new Carat15Deposit());
		deposits.add(new GoldenFleeceDeposit());
		deposits.add(new ForGiftsDeposit());
		deposits.add(new MagicianPlusDepositBYR(9));
		deposits.add(new MagicianPlusDepositBYR(12));
		deposits.add(new MagicianPlusDepositCUR(9));
		deposits.add(new MagicianPlusDepositCUR(12));
	}

	public Deposit[] GetAllDeposits(Currency currency) {
		List<Deposit> list = new ArrayList<Deposit>();

		for (Deposit deposit : deposits) {
			if (deposit.currency == currency
					|| (currency == Currency.CUR && (deposit.currency == Currency.EUR || deposit.currency == Currency.USD))) {
				list.add(deposit);
			}
		}

		return list.toArray(new Deposit[list.size()]);
	}
}
