package bali.time;

/**
 * User: Richard
 * Date: 11/07/13
 */
public interface CalendarSystem {

	public Date getDate(Instant instant);

	public Time getTime(Instant instant);

	public DateTime getDateTime(Instant instant);

	public Instant getInstant(Date date);

	public Instant getInstant(DateTime date);
}
