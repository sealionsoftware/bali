package bali.time;

import bali.Number;
import bali.Quantified;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Interval extends Quantified<Interval> {

	public Number getTimeInMillis();

}
