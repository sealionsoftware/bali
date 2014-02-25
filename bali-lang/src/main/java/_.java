import bali.Number;
import bali.ReaderWriter;
import bali.Serializer;
import bali.annotation.ThreadSafe;
import bali.io.NetworkManager;
import bali.time.Clock;
import bali.time.TimeZone;

/**
 * This stub is provided so that classes can be compiled against bali-lang. The real constants class is provided at
 * runtime by bali-runtime, which must have all the same declarations and be placed first on the classpath. I know this
 * is not ideal and am searching for a better solution.
 *
 * User: Richard
 * Date: 29/04/13
 */
public final class _ {

	@ThreadSafe
	public static final ReaderWriter CONSOLE = null;

	@ThreadSafe
	public static final Serializer<Number> NUMBER_FACTORY = null;

	@ThreadSafe
	public static final TimeZone LOCAL_TIMEZONE = null;

	@ThreadSafe
	public static final Clock SYSTEM_CLOCK = null;

	@ThreadSafe
	public static final NetworkManager NETWORK_MANAGER = null;


}
