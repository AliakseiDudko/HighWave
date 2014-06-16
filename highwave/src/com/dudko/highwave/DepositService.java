package com.dudko.highwave;

import java.util.ArrayList;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;

@Api(name = "deposits", version = "v0")
public class DepositService {
	public static ArrayList<Deposit> deposits = new ArrayList<Deposit>();
	public static ArrayList<Bank> banks = new ArrayList<Bank>();

	static {
		deposits.add(new Deposit("Deposit 1.1", 0, 0));
		deposits.add(new Deposit("Deposit 1.2", 1, 0));
		deposits.add(new Deposit("Deposit 1.3", 2, 0));
		deposits.add(new Deposit("Deposit 2.1", 3, 1));
		deposits.add(new Deposit("Deposit 2.2", 4, 1));
		deposits.add(new Deposit("Deposit 3.1", 5, 2));

		banks.add(new Bank("HomeCreditBank", 1));
		banks.add(new Bank("IdeaBank", 2));
		banks.add(new Bank("BelAgroPromBank", 3));
	}

	@ApiMethod(name = "get.deposits.list", path = "deposits", httpMethod = HttpMethod.GET)
	public ArrayList<Deposit> getAllDeposites() {
		return deposits;
	}

	@ApiMethod(name = "get.deposit.by.deposit.id", path = "deposits/{depositId}", httpMethod = HttpMethod.GET)
	public Deposit getDeposit(@Named("depositId") Integer depositId) {
		return deposits.get(depositId);
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

		for (int i = 0; i < deposits.size(); i++) {
			Deposit deposit = deposits.get(i);
			if (deposit.bankId == bankId) {
				bankDeposits.add(deposit);
			}
		}

		return bankDeposits;
	}
}
