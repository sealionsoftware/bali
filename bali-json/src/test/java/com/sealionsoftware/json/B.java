package com.sealionsoftware.json;

import bali.Integer;
import bali.annotation.Name;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 17/03/14
 */
public class B {

	public Integer aNumber;
	public A a;

	public B() {
	}

	@Parameters
	public B(@Name("aNumber") Integer aNumber, @Name("a") A a) {
		this.aNumber = aNumber;
		this.a = a;
	}
}
