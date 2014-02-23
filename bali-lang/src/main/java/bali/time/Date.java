package bali.time;

import bali.Number;
import bali.String;
import bali.annotation.MetaType;
import bali.annotation.Kind;

/**
 * User: Richard
 * Date: 15/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Date {

	public String getEpoch();

	public Number getYear();

	public Number getMonth();

	public Number getDay();
}
