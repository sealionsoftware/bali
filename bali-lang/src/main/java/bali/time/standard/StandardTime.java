package bali.time.standard;

import bali.Number;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.time.Time;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(Kind.OBJECT)
public final class StandardTime implements Time {

	private Number hour;
	private Number minute;
	private Number second;
	private Number milliseconds;

	public StandardTime(Number hour, Number minute, Number second, Number milliseconds) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.milliseconds = milliseconds;
	}

	public Number getHour(){
		return hour;
	}

	public Number getMinute(){
		return minute;
	}

	public Number getSecond(){
		return second;
	}

	public Number getMilliseconds(){
		return milliseconds;
	}

}
