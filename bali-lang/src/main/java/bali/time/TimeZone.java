package bali.time;

import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(Kind.INTERFACE)
public interface TimeZone {

	public String getName();

	public Interval getOffset();

}
