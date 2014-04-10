package com.sealionsoftware.json;

import bali.Boolean;
import bali.String;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 17/03/14
 */
public class A {

	public A() {
	}

	@Parameters
	public A(@Name("aString") @Nullable String aString,
	         @Name("aBoolean") @Nullable Boolean aBoolean,
	         @Name("b") @Nullable B b) {
		this.aString = aString;
		this.aBoolean = aBoolean;
		this.b = b;
	}

	@Nullable
	public String aString;
	@Nullable
	public Boolean aBoolean;
	@Nullable
	public B b;
}
