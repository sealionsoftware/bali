package bali.time;

import bali.Operator;
import bali.Quantified;
import bali.Number;

/**
 * Represents an instant in universal time as milliseconds since the start of 1970 UTC
 *
 * User: Richard
 * Date: 11/07/13
 */
public interface Instant extends Quantified<Instant> {

	@Operator("+")
	public Instant add(Interval interval);

	@Operator("-")
	public Instant subtract(Interval interval);

	public Number getMillisSince1970();

}