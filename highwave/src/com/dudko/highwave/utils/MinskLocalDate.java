package com.dudko.highwave.utils;

import org.joda.time.*;

public class MinskLocalDate {
	private static DateTimeZone minskTimeZone;

	static {
		minskTimeZone = DateTimeZone.forID("Europe/Minsk");
	}

	public static LocalDate now() {
		return LocalDate.now(minskTimeZone);
	}
}