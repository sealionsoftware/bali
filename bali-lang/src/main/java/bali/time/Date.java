package bali.time;

import bali.Number;
import bali.String;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 15/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Date {

	public String getEpoch();

	public Number getYear();

	public Number getMonth();

	public Number getDay();
}
