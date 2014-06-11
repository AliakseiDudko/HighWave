package com.dudko.highwave;

import java.util.ArrayList;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

@Api(name = "deposites", version = "v0")
public class DepositService {
	public static ArrayList<Deposit> deposites = new ArrayList<Deposit>();

	static {
		deposites.add(new Deposit("Deposit 1.1", 1));
		deposites.add(new Deposit("Deposit 1.2", 1));
		deposites.add(new Deposit("Deposit 1.3", 1));
		deposites.add(new Deposit("Deposit 2.1", 2));
		deposites.add(new Deposit("Deposit 2.2", 2));
		deposites.add(new Deposit("Deposit 3.1", 3));
	}

	@ApiMethod(name = "deposites1488", httpMethod = "get")
	public ArrayList<Deposit> getAllDeposites() {
		return deposites;
	}

	@ApiMethod(name = "deposit")
	public Deposit getDeposit(@Named("depositId") Integer depositId) {
		return deposites.get(depositId);
	}
}
