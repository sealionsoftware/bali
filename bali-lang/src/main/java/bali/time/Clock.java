package bali.time;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 12/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Clock {

	public Instant getNow();

	public Date getDate();

	public Time getTime();

	public DateTime getDateAndTime();

}
