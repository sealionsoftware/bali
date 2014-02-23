package bali.time;

import bali.Number;
import bali.Quantified;
import bali.annotation.MetaType;
import bali.annotation.Kind;
import bali.annotation.Operator;

/**
 * Represents an instant in universal time as milliseconds since the start of 1970 UTC
 *
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Instant extends Quantified<Instant> {

	@Operator("+")
	public Instant add(Interval interval);

	@Operator("-")
	public Instant subtract(Interval interval);

	public Number getMillisSince1970();

}
