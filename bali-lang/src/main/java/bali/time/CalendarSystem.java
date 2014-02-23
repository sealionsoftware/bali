package bali.time;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(Kind.INTERFACE)
public interface CalendarSystem {

	public Date getDate(Instant instant);

	public Time getTime(Instant instant);

	public DateTime getDateTime(Instant instant);

	public Instant getInstant(Date date);

	public Instant getInstant(DateTime date);
}
