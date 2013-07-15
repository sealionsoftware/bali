package bali;

import bali.time.Clock;
import bali.time.TimeZone;
import com.sealionsoftware.bali.CharArrayString;
import bali.time.standard.StandardInterval;
import bali.time.standard.StandardTimeZone;
import com.sealionsoftware.bali.SystemClock;
import bali.time.calendar.ISOCalendarSystem;
import com.sealionsoftware.bali.Console;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class _ {

	public static final ReaderWriter CONSOLE = new Console();

	public static final Serializer<Number> NUMBER_FACTORY = com.sealionsoftware.bali._.NUMBER_FACTORY;

	public static final TimeZone LOCAL_TIMEZONE = new StandardTimeZone(
			new CharArrayString("GMT".toCharArray()),
			new StandardInterval(NUMBER_FACTORY.parse(new CharArrayString("3600000".toCharArray())))
	);

	public static final Clock SYSTEM_CLOCK = new SystemClock(new ISOCalendarSystem(), LOCAL_TIMEZONE);


}
