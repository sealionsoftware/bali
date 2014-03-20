package com.sealionsoftware.json;

import bali.String;
import bali.Boolean;
import bali.annotation.Name;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 17/03/14
 */
public class A {

	public A() {
	}

	@Parameters
	public A(@Name("aString") String aString,
	         @Name("aBoolean") Boolean aBoolean,
	         @Name("b") B b) {
		this.aString = aString;
		this.aBoolean = aBoolean;
		this.b = b;
	}

	public String aString;
	public Boolean aBoolean;
	public B b;
}
