package bali.time;

import bali.Number;
import bali.Quantified;

/**
 * User: Richard
 * Date: 11/07/13
 */
public interface Interval extends Quantified<Interval> {

	public Number getTimeInMillis();

}
