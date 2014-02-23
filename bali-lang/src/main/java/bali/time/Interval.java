package bali.time;

import bali.Number;
import bali.Quantified;
import bali.annotation.MetaType;
import bali.annotation.Kind;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Interval extends Quantified<Interval> {

	public Number getTimeInMillis();

}
