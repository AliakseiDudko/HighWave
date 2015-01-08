package com.dudko.highwave.utils;

import org.joda.time.*;

public class Constants {
	public static LocalDate tax30StartDay;
	public static LocalDate tax20StartDay;
	public static LocalDate tax10StartDay;
	public static LocalDate taxEndDay;

	static {
		tax30StartDay = new LocalDate("2014-12-20");
		tax20StartDay = new LocalDate("2014-12-30");
		tax10StartDay = new LocalDate("2015-01-06");
		taxEndDay = new LocalDate("2015-01-09");
	}
}