package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Operator;
import bali.annotation.ThreadSafe;

/**
 * User: Richard
 * Date: 07/11/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Count {

	@Operator("++")
	@ThreadSafe
	public Integer increment();

	@Operator("--")
	@ThreadSafe
	public Integer decrement();

	@Operator("#")
	@ThreadSafe
	public Integer value();


}
