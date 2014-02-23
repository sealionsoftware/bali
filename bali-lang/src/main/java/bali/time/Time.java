package bali.time;

import bali.Number;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 15/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Time {

	public Number getHour();

	public Number getMinute();

	public Number getSecond();

	public Number getMilliseconds();
}
