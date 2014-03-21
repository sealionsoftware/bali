package com.sealionsoftware.json;

import bali.Integer;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 17/03/14
 */
public class B {

	@Nullable
	public Integer aNumber;
	@Nullable
	public A a;

	public B() {
	}

	@Parameters
	public B(@Name("aNumber") @Nullable Integer aNumber, @Name("a") @Nullable A a) {
		this.aNumber = aNumber;
		this.a = a;
	}
}
