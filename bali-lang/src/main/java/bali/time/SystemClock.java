package bali.time;

import bali.Number;
import bali.annotation.MetaType;
import bali.annotation.Kind;
import bali.time.standard.StandardDateTime;
import bali.time.standard.StandardInstant;

import static bali.number.NumberFactory.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 12/07/13
 */
@MetaType(Kind.OBJECT)
public final class SystemClock implements Clock {

	private final CalendarSystem calendar;
	private final TimeZone timezone;

	public SystemClock(CalendarSystem calendar, TimeZone timezone) {
		this.calendar = calendar;
		this.timezone = timezone;
	}

	public Instant getNow() {
		Number now = NUMBER_FACTORY.forLong(System.currentTimeMillis());
		return new StandardInstant(now);
	}

	public Date getDate() {
		return calendar.getDate(getAdjustedInstant());
	}

	public Time getTime() {
		return calendar.getTime(getAdjustedInstant());
	}

	private Instant getAdjustedInstant(){
		return getNow().add(timezone.getOffset());
	}

	public DateTime getDateAndTime() {

		Instant now = getNow();
		Date date = calendar.getDate(now);
		Time time = calendar.getTime(now);
		return new StandardDateTime(date, time);
	}
}
