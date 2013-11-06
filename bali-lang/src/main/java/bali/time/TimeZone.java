package bali.time;

import bali.String;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface TimeZone {

	public String getName();

	public Interval getOffset();

}
