package example;

import com.sealionsoftware.bali.IdentityBoolean;

public class CastObject {

	public void goDo() {

		Object a = IdentityBoolean.TRUE;
		java.lang.Boolean b = (java.lang.Boolean) a;

	}

}
