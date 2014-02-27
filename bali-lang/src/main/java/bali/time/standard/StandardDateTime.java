package bali.time.standard;

import bali.Number;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
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

	public String getEpoch() {
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

	public Number getHour() {
		return time.getHour();
	}

	public Number getMinute() {
		return time.getMinute();
	}

	public Number getSecond() {
		return time.getSecond();
	}

	public Number getMilliseconds() {
		return time.getMilliseconds();
	}
}
