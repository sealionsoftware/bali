package bali.util;

import bali.*;
import bali.Boolean;
import com.sealionsoftware.bali.IdentityBoolean;

/**
 * User: Richard
 * Date: 11/07/13
 */
public class StandardCounter implements Counter {

	private int count = 0;

	@Operator("++")
	public void increment(){
		count++;
	}

	@Operator("--")
	public void decrement(){
		count--;
	}

	public bali.Number value(){
		return _.NUMBER_FACTORY.forInt(count);
	}

	public Boolean greaterThan(StandardCounter o) {
		return count > o.count ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
	}

	public Boolean lessThan(StandardCounter o) {
		return count < o.count ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
	}

	public Boolean equalTo(StandardCounter o) {
		return count == o.count ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
	}
}
