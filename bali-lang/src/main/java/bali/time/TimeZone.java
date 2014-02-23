package bali.time;

import bali.String;
import bali.annotation.MetaType;
import bali.annotation.Kind;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(Kind.INTERFACE)
public interface TimeZone {

	public String getName();

	public Interval getOffset();

}
