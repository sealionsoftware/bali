package bali.time;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface CalendarSystem {

	public Date getDate(Instant instant);

	public Time getTime(Instant instant);

	public DateTime getDateTime(Instant instant);

	public Instant getInstant(Date date);

	public Instant getInstant(DateTime date);
}
