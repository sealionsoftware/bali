package bali.time;

import bali.Number;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 15/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Time {

	public Number getHour();

	public Number getMinute();

	public Number getSecond();

	public Number getMilliseconds();
}
