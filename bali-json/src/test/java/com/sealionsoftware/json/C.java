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
public class C {

	@Parameters
	public C(@Name("aString") String aString) {
		this.aString = aString;
	}

	public String aString;
}
