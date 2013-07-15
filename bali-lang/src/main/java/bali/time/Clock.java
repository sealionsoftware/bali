package bali.time;

/**
 * User: Richard
 * Date: 12/07/13
 */
public interface Clock {

	public Instant getNow();

	public Date getDate();

	public Time getTime();

	public DateTime getDateAndTime();

}
