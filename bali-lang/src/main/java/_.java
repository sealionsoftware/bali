import bali.CharArrayString;
import bali.Console;
import bali.Number;
import bali.ReaderWriterMonitor;
import bali.Serializer;
import bali.number.NumberFactory;
import bali.time.Clock;
import bali.time.SystemClock;
import bali.time.TimeZone;
import bali.time.calendar.ISOCalendarSystem;
import bali.time.standard.StandardInterval;
import bali.time.standard.StandardTimeZone;

/**
 * User: Richard
 * Date: 29/04/13
 */
public final class _ {

	public static final ReaderWriterMonitor CONSOLE = new Console();

	public static final Serializer<Number> NUMBER_FACTORY = NumberFactory.NUMBER_FACTORY;

	public static final TimeZone LOCAL_TIMEZONE = new StandardTimeZone(
			new CharArrayString("GMT".toCharArray()),
			new StandardInterval(NumberFactory.NUMBER_FACTORY.forInt(3600000))
	);

	public static final Clock SYSTEM_CLOCK = new SystemClock(new ISOCalendarSystem(), LOCAL_TIMEZONE);


}
