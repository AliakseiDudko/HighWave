package com.dudko.highwave.deposit;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AccountStatementRecord {
	public String date;
	public float amount;
	public float interest;
	public String description;
	
	public static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/YY");
	
	public AccountStatementRecord(DateTime date, float amount, float interest, String description){
		this.date = date.toString(dateFormatter);
		this.amount = amount;
		this.interest = interest;
		this.description = description;
	}
}
