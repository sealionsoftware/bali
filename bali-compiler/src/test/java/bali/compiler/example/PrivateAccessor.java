package bali.compiler.example;

import java.lang.reflect.Field;

/**
 * User: Richard
 * Date: 15/05/13
 */
public class PrivateAccessor {

	public boolean accessPrivate() throws Exception{

		Boolean b = Boolean.FALSE;
		Field f = Boolean.class.getDeclaredField("value");
		f.setAccessible(true);
		return f.getBoolean(b);
	}

}
