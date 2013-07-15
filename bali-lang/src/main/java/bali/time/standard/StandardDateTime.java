package bali.time.standard;

import bali.Number;
import bali.time.Date;
import bali.time.DateTime;
import bali.time.Time;

/**
 * User: Richard
 * Date: 15/07/13
 */
public final class StandardDateTime implements DateTime {

	private Date date;
	private Time time;

	public StandardDateTime(Date date, Time time) {
		this.date = date;
		this.time = time;
	}

	public bali.String getEpoch() {
		return date.getEpoch();
	}

	public Number getYear() {
		return date.getYear();
	}

	public Number getMonth() {
		return date.getMonth();
	}

	public Number getDay() {
		return date.getDay();
	}

	public java.lang.Number getHour() {
		return time.getHour();
	}

	public java.lang.Number getMinute() {
		return time.getMinute();
	}

	public java.lang.Number getSecond() {
		return time.getSecond();
	}

	public java.lang.Number getMilliseconds() {
		return time.getMilliseconds();
	}
}
