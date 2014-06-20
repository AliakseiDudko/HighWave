package com.dudko.highwave;

import java.util.ArrayList;

import javax.inject.Named;

import com.dudko.highwave.deposit.DepositFactory;
import com.dudko.highwave.deposit.deposits.Deposit;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;

@Api(name = "deposits", version = "v0")
public class DepositService {
	public static DepositFactory depositFactory;

	public static ArrayList<Bank> banks = new ArrayList<Bank>();

	static {
		depositFactory = new DepositFactory();

		banks.add(new Bank("HomeCreditBank", 1));
		banks.add(new Bank("IdeaBank", 2));
		banks.add(new Bank("BelAgroPromBank", 3));
	}

	@ApiMethod(name = "get.deposits.list", path = "deposits", httpMethod = HttpMethod.GET)
	public ArrayList<Deposit> getAllDeposites() {
		return depositFactory.GetAllDeposits();
	}

	@ApiMethod(name = "get.deposit.by.deposit.id", path = "deposits/{depositId}", httpMethod = HttpMethod.GET)
	public Deposit getDeposit(@Named("depositId") Integer depositId) {
		DepositFactory factory = new DepositFactory();
		return factory.GetDeposit(depositId);
	}

	@ApiMethod(name = "get.banks.list", path = "banks", httpMethod = HttpMethod.GET)
	public ArrayList<Bank> getAllBanks() {
		return banks;
	}

	@ApiMethod(name = "get.bank.by.bank.id", path = "banks/{bankId}", httpMethod = HttpMethod.GET)
	public Bank getBank(@Named("bankId") Integer bankId) {
		return banks.get(bankId);
	}

	@ApiMethod(name = "get.deposits.list.by.bank.id", path = "banks/{bankId}/deposits", httpMethod = HttpMethod.GET)
	public ArrayList<Deposit> getBankDeposites(@Named("bankId") Integer bankId) {
		ArrayList<Deposit> bankDeposits = new ArrayList<Deposit>();
		ArrayList<Deposit> deposits = depositFactory.GetAllDeposits();

		for (int i = 0; i < deposits.size(); i++) {
			Deposit deposit = deposits.get(i);
			if (true) {
				bankDeposits.add(deposit);
			}
		}

		return bankDeposits;
	}
}
