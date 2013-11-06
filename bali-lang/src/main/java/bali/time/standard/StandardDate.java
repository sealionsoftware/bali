package bali.time.standard;

import bali.Number;
import bali.String;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.time.Date;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(MetaTypes.CLASS)
public final class StandardDate implements Date {

	private String epoch;
	private Number year;
	private Number month;
	private Number day;

	public StandardDate(String epoch, Number year, Number month, Number day) {
		this.epoch = epoch;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public String getEpoch(){
		return epoch;
	}

	public Number getYear(){
		return year;
	}

	public Number getMonth(){
		return month;
	}

	public Number getDay(){
		return day;
	}
}

