package com.dudko.highwave.deposit;

import org.joda.time.DateTime;

public class AccountStatementRecord {
	public String date;
	public float amount;
	public float interest;
	public String description;
	
	public AccountStatementRecord(DateTime date, float amount, float interest, String description){
		this.date = date.toString();
		this.amount = amount;
		this.interest = interest;
		this.description = description;
	}
	
	public AccountStatementRecord(DateTime date, float amount, float interest){
		this.date = date.toString();
		this.amount = amount;
		this.interest = interest;
		this.description = "";
	}
}
