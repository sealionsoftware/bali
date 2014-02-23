package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Operator;
import bali.annotation.ThreadSafe;

/**
 * User: Richard
 * Date: 07/11/13
 */
@MetaType(Kind.INTERFACE)
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
