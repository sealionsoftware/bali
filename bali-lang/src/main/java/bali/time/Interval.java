package bali.time;

import bali.Number;
import bali.Quantified;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Interval extends Quantified<Interval> {

	public Number getTimeInMillis();

}
