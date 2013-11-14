package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 07/11/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Count {

	@Operator("++")
	public Integer increment();

	@Operator("--")
	public Integer decrement();

	@Operator("#")
	public Integer value();


}
