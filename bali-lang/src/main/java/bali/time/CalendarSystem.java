package bali.time;

import bali.time.standard.StandardDate;
import bali.time.standard.StandardTime;

/**
 * User: Richard
 * Date: 11/07/13
 */
public interface CalendarSystem {

	public StandardDate getDate(Instant instant);

	public StandardTime getTime(Instant instant);

	public Instant getInstant(StandardDate date);

	public Instant getInstant(StandardDate date, StandardTime time);
}
