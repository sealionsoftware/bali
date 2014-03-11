import bali.CharArrayString;
import bali.Console;
import bali.Number;
import bali.ReaderWriter;
import bali.Serializer;
import bali.annotation.ThreadSafe;
import bali.file.FileManager;
import bali.file.StandardFileManager;
import bali.net.NetworkManager;
import bali.net.StandardNetworkManager;
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

	@ThreadSafe
	public static final ReaderWriter CONSOLE = new Console();

	@ThreadSafe
	public static final Serializer<Number> NUMBER_FACTORY = NumberFactory.NUMBER_FACTORY;

	@ThreadSafe
	public static final TimeZone LOCAL_TIMEZONE = new StandardTimeZone(
			new CharArrayString("GMT".toCharArray()),
			new StandardInterval(NumberFactory.NUMBER_FACTORY.forInt(3600000))
	);

	@ThreadSafe
	public static final Clock SYSTEM_CLOCK = new SystemClock(new ISOCalendarSystem(), LOCAL_TIMEZONE);

	@ThreadSafe
	public static final NetworkManager NETWORK_MANAGER = new StandardNetworkManager();

	@ThreadSafe
	public static final FileManager FILE_MANAGER = new StandardFileManager();


}
