package bali.time.calendar;

import com.sealionsoftware.bali.CharArrayString;
import bali.Number;
import bali.String;
import bali.time.CalendarSystem;
import bali.time.standard.StandardDate;
import bali.time.Instant;
import bali.time.standard.StandardTime;

/**
 * Implements a proleptic Gregorian Calendar.
 *
 * User: Richard
 * Date: 11/07/13
 */
public final class ISOCalendarSystem implements CalendarSystem {

	private static final int YEAR_LENGTH = 365;
	private static final int LEAP_YEAR_LENGTH = 366;

	private static final String COMMON_ERA = new CharArrayString("CE".toCharArray());
	private static final String BEFORE_COMMON_ERA = new CharArrayString("BCE".toCharArray());

	public StandardDate getDate(Instant instant) {

		Number millis = instant.getMillisSince1970();
		final Number days;
		final Number months;
		final Number years;

//		Number year = 1970;
//		int millisInYear = getDaysInYear(year);
//		while (millis.lessThan(millisInYear)){
//			millis -= millisInYear;
//			millisInYear = getDaysInYear(year++);
//		}


//		return new Date(COMMON_ERA, years, months, days);
		return null;
	}

	private int getDaysInYear(int year){
		if (year % 4 == 0){
			if (year % 100 == 0){
				if (year % 400 != 0){
					return LEAP_YEAR_LENGTH;
				}
				return YEAR_LENGTH;
			}
			return LEAP_YEAR_LENGTH;
		}
		return YEAR_LENGTH;
	}

	public StandardTime getTime(Instant instant) {
		return null;
	}

	public Instant getInstant(StandardDate date) {
		return getInstant(date, null);
	}

	public Instant getInstant(StandardDate date, StandardTime time) {

		long ret = 0;

		return null;
	}
}
