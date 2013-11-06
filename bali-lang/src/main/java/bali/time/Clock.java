package bali.time;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 12/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Clock {

	public Instant getNow();

	public Date getDate();

	public Time getTime();

	public DateTime getDateAndTime();

}
