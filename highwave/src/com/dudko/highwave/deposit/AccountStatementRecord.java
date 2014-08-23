package com.dudko.highwave.deposit;

import org.joda.time.DateTime;
import org.joda.time.format.*;

public class AccountStatementRecord {
	public String date;
	public float amount;
	public float interest;
	public String description;
	public boolean isLast;

	private static DateTimeFormatter dateFormatter = ISODateTimeFormat.date();

	public AccountStatementRecord(DateTime date, float amount, float interest, String description) {
		this.date = date.toString(dateFormatter);
		this.amount = amount;
		this.interest = interest;
		this.description = description;
	}

	public AccountStatementRecord setIsLast(Boolean isLast) {
		this.isLast = isLast;
		return this;
	}

	public DateTime GetOriginalDate() {
		return dateFormatter.parseDateTime(date);
	}
}