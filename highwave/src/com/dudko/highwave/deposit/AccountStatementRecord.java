package com.dudko.highwave.deposit;

import org.joda.time.*;
import org.joda.time.format.*;

import com.dudko.highwave.globalize.RecordDescriptions;

public class AccountStatementRecord {
	public String date;
	public float amount;
	public float interest;
	public RecordDescriptions description;
	public boolean isLast;

	private static DateTimeFormatter dateFormatter = ISODateTimeFormat.date();

	public AccountStatementRecord(LocalDate date, float amount, float interest, RecordDescriptions description) {
		this.date = date.toString(dateFormatter);
		this.amount = amount;
		this.interest = interest;
		this.description = description;
	}

	public AccountStatementRecord setIsLast(Boolean isLast) {
		this.isLast = isLast;
		return this;
	}

	public LocalDate GetOriginalDate() {
		return dateFormatter.parseLocalDate(date);
	}
}