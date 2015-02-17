package bali.compiler.type;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class ClassLibrary {

	private Map<String, Class> classes = new HashMap<>();

	public Class getClass(String fullyQualifiedClassName) {
		return classes.get(fullyQualifiedClassName);
	}

}
